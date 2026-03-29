package com.example.clothing4413.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clothing4413.dto.CheckoutRequest;
import com.example.clothing4413.model.Cart;
import com.example.clothing4413.model.CartItem;
import com.example.clothing4413.model.Customer;
import com.example.clothing4413.model.Order;
import com.example.clothing4413.model.OrderItem;
import com.example.clothing4413.model.Product;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.repository.CartRepository;
import com.example.clothing4413.repository.OrderRepository;
import com.example.clothing4413.repository.ProductRepository;
import com.example.clothing4413.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository, PaymentService paymentService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
        this.productRepository = productRepository;
    }

    //Returns a list of all orders from a customer
    public List<Order> getOrderByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    //Checks out a cart using customer's billing information from checkoutRequest
    public Order checkout(CheckoutRequest request) {
        Users user = userRepository.findUsersById(request.getCustomerId());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (!(user instanceof Customer)) {
            throw new IllegalStateException("User is not a customer");
        }

        Customer customer = (Customer) user;

        Cart cart = cartRepository.findByCustomerId(request.getCustomerId());
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty"); //This should never happen due to how frontend hides checkout if cart is empty but just in case
        }

        //Get total cost of the cart
        double total = 0;
        for (CartItem item : cart.getItems()) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        //Process the payment
        boolean paymentOK = paymentService.processPayment(request.getCardNumber(), request.getCardExpiry(), request.getCardHolderName(), total);
        if (!paymentOK) {
            throw new IllegalStateException("Payment Authorization Failed, try again");
        }

        //If they check the "save info" button, save their billing information
        if (request.isSaveInfo()) {
            saveBillingInfo(customer, request.getShippingAddress(), request.getBillingAddress(), request.getCardNumber(), request.getCardHolderName(), request.getCardExpiry());
            userRepository.saveAndFlush(customer);
        }

        //Create Order and save it to the db
        Order order = new Order(customer);
        orderRepository.saveAndFlush(order);

        //Convert all items in the cart to order items and add them to the order
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity());
            order.getItems().add(orderItem);

            //When an Item gets added to the order, the stock of that item has to go down as well
            Product product = cartItem.getProduct();
            int newStock = product.getStock() - cartItem.getQuantity();
            if (newStock < 0) {
                throw new IllegalStateException(product.getName() + " Stock is below 0"); //Should never happen since the cart bounds how much of a product you can add by stock
            }
            product.setStock(newStock);
            productRepository.saveAndFlush(product);

        }

        //Update the order with all items inside
        orderRepository.saveAndFlush(order);

        //Clear cart
        cart.getItems().clear();
        cartRepository.saveAndFlush(cart);

        return order;
    }

    //Method that sets customer payment info
    private void saveBillingInfo(Customer customer, String shippingAddress, String billingAddress, String cardNumber, String cardHolderName, String cardExpiry) {
        customer.setShippingAddress(shippingAddress);
        customer.setBillingAddress(billingAddress);
        customer.setCardNumber(cardNumber);
        customer.setCardHolderName(cardHolderName);
        customer.setCardExpiry(cardExpiry);
    }
}