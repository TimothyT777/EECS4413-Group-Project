package com.example.clothing4413.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.clothing4413.dto.AdminSalesItemResponse;
import com.example.clothing4413.dto.AdminSalesOrderResponse;
import com.example.clothing4413.dto.CheckoutRequest;
import com.example.clothing4413.model.Cart;
import com.example.clothing4413.model.CartItem;
import com.example.clothing4413.model.Customer;
import com.example.clothing4413.model.Order;
import com.example.clothing4413.model.OrderItem;
import com.example.clothing4413.model.Product;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.repository.CartItemRepository;
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
    private final CartItemRepository cartItemRepository;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            UserRepository userRepository,
            PaymentService paymentService,
            ProductRepository productRepository,
            CartItemRepository cartItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // Returns a list of all orders from a customer
    public List<Order> getOrderByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // Admin sales history with optional filters
    public List<AdminSalesOrderResponse> getSalesHistory(
            Long customerId,
            Long productId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return orderRepository.findAll()
                .stream()
                .filter(order -> customerId == null || order.getCustomer().getId().equals(customerId))
                .filter(order -> startDate == null || !order.getCreatedAt().toLocalDate().isBefore(startDate))
                .filter(order -> endDate == null || !order.getCreatedAt().toLocalDate().isAfter(endDate))
                .map(order -> buildAdminSalesOrderResponse(order, productId))
                .filter(orderResponse -> !orderResponse.getItems().isEmpty())
                .sorted(Comparator.comparing(AdminSalesOrderResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    private AdminSalesOrderResponse buildAdminSalesOrderResponse(Order order, Long productId) {
        List<AdminSalesItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            if (productId != null && !item.getProduct().getProduct_id().equals(productId)) {
                continue;
            }

            AdminSalesItemResponse itemResponse = new AdminSalesItemResponse();
            itemResponse.setOrderItemId(item.getId());
            itemResponse.setProductId(item.getProduct().getProduct_id());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setProductBrand(item.getProduct().getBrand());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPriceAtPurchase(item.getPriceAtPurchase());

            itemResponses.add(itemResponse);
        }

        AdminSalesOrderResponse response = new AdminSalesOrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getName());
        response.setCustomerEmail(order.getCustomer().getEmail());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(itemResponses);

        return response;
    }

    // Checks out a cart using customer's billing information from checkoutRequest
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
            throw new IllegalStateException("Cart is empty");
        }

        double total = 0;
        for (CartItem item : cart.getItems()) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        boolean paymentOK = paymentService.processPayment(
                request.getCardNumber(),
                request.getCardExpiry(),
                request.getCardHolderName(),
                total
        );

        if (!paymentOK) {
            throw new IllegalStateException("Payment Authorization Failed, try again");
        }

        if (request.isSaveInfo()) {
            saveBillingInfo(
                    customer,
                    request.getShippingAddress(),
                    request.getBillingAddress(),
                    request.getCardNumber(),
                    request.getCardHolderName(),
                    request.getCardExpiry()
            );
            userRepository.saveAndFlush(customer);
        }

        Order order = new Order(customer);
        orderRepository.saveAndFlush(order);

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity());
            order.getItems().add(orderItem);

            Product product = cartItem.getProduct();
            int newStock = product.getStock() - cartItem.getQuantity();
            if (newStock < 0) {
                throw new IllegalStateException(product.getName() + " Stock is below 0");
            }
            product.setStock(newStock);
            productRepository.saveAndFlush(product);

            syncCartsAfterCheckout(product, newStock);
        }

        orderRepository.saveAndFlush(order);

        cart.getItems().clear();
        cartRepository.saveAndFlush(cart);

        return order;
    }

    private void saveBillingInfo(
            Customer customer,
            String shippingAddress,
            String billingAddress,
            String cardNumber,
            String cardHolderName,
            String cardExpiry
    ) {
        customer.setShippingAddress(shippingAddress);
        customer.setBillingAddress(billingAddress);
        customer.setCardNumber(cardNumber);
        customer.setCardHolderName(cardHolderName);
        customer.setCardExpiry(cardExpiry);
    }

    private void syncCartsAfterCheckout(Product product, int newStock) {
        List<CartItem> affectedCartItems = cartItemRepository.findByProduct(product);

        for (CartItem cartItem : affectedCartItems) {
            if (newStock == 0) {
                //Remove item from all carts since the stock of the product is 0
                cartItem.getCart().getItems().remove(cartItem);
                cartItemRepository.delete(cartItem);
            } else if (cartItem.getQuantity() > newStock) {
                //The user has more of the product than is currently in stock in their cart, reduce the amount to match new stock
                cartItem.setQuantity(newStock);
                cartItemRepository.saveAndFlush(cartItem);
            }
        }
    }
}