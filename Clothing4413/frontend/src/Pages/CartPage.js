import { useState, useEffect} from 'react';
import { useAuth } from "../Context/AuthContext";
import { useNavigate } from 'react-router-dom';
import "../Styles/CartPage.css";

function CartPage() {
	const { user } = useAuth(); // Access user from AuthContext
    const [cartItems, setCartItems] = useState([]); // State to hold cart items
    const [loading, setLoading] = useState(true); // Tracks if the items in the cart have beenn fetched
    const [total, setTotal] = useState(0); // Total price of items in the cart
    const [itemCount, setItemCount] = useState(0); //Total number of items in the cart
    const navigate = useNavigate();

    useEffect(() => {
        if (!user) return;
        const fetchCart = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/cart/${user.id}`, {
                    credentials: "include"
                });

                const data = await response.json();
                setCartItems(data.items || []);
                setTotal(data.total || 0);
                setItemCount(data.itemCount || 0);
            } catch (error) {
                console.error("Error fetching cart:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchCart();
    }, [user]);

	const handleRemove = async (productId) => {
        try {
            await fetch(`http://localhost:8080/api/cart/remove`, {
                method: "DELETE",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({ customerId: user.id, productId })
            });
            setCartItems(cartItems.filter(item => item.product.id !== productId));
        } catch (error) {
            console.error("Error removing item:", error);
        }
    };

    const handleUpdateQuantity = async (productId, newQuantity) => {
        // Find the cart item
        const item = cartItems.find(i => i.product.id === productId);
        if (!item) return;

        // Prevent exceeding stock
        if (newQuantity > item.product.stock) {
            alert(`Cannot add more than ${item.product.stock} of this product.`);
            return;
        }

        // Remove if quantity <= 0
        if (newQuantity <= 0) {
            handleRemove(productId);
            return;
        }

        try {
            // Send update to backend
            await fetch(`http://localhost:8080/api/cart/update`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({
                    customerId: user.id,
                    productId,
                    quantity: newQuantity
                })
            });

            // Update state locally
            const updatedItems = cartItems.map(i =>
                i.product.id === productId ? { ...i, quantity: newQuantity } : i
            );

            setCartItems(updatedItems);

            // Recalculate total and itemCount
            const newTotal = updatedItems.reduce(
                (total, i) => total + i.product.price * i.quantity,
                0
            );
            setTotal(newTotal);
            const newItemCount = updatedItems.reduce((count, i) => count + i.quantity, 0);
            setItemCount(newItemCount);
        } catch (error) {
            console.error("Error updating quantity:", error);
        }
    };

    //Actual Cart page:
    if (!user) {
        return (
            <div className="cart-page">
                <p className="cart-message">You must be logged in to view your cart.</p>
            </div>
        );
    }

    if (loading) {
        return (
            <div className="cart-page">
                <p className="cart-message">Loading cart...</p>
            </div>
        );
    }

    if (cartItems.length === 0) {
        return (
            <div className="cart-page">
                <p className="cart-message">Your cart is empty.</p>
            </div>
        );
    }

    return (
        <div className="cart-page">
            <h1 className="cart-title">Your Cart</h1>
            <div className="cart-container">
                <div className="cart-items">
                    {cartItems.map((item) => (
                        <div className="cart-card" key={item.id}>
                            <img src={item.product.image} alt={item.product.name} className="cart-image"/>
                            <div className="cart-info">
                                <p className="cart-name">{item.product.name}</p>
                                <p className="cart-brand">{item.product.brand}</p>
                                <p className="cart-category">{item.product.category}</p>
                                <p className="cart-description">{item.product.description}</p>
                                <p className="cart-price">${item.product.price}</p>
                            </div>
                            <div className="cart-actions">
                                <div className="cart-quantity">
                                    <button onClick={() => handleUpdateQuantity(item.product.id, item.quantity - 1)}>-</button>
                                    <span>{item.quantity}</span>
                                    <button onClick={() => handleUpdateQuantity(item.product.id, item.quantity + 1)}>+</button>
                                </div>
                                <p className="cart-subtotal">Subtotal: ${(item.product.price * item.quantity).toFixed(2)}</p>
                                <button className="cart-remove" onClick={() => handleRemove(item.product.id)}>Remove</button>
                            </div>
                        </div>
                    ))}
                </div>
                <div className="cart-summary">
                    <h2>Order Summary</h2>
                    <p>Total Items: {itemCount}</p>
                    <p className="cart-total">Total: ${total.toFixed(2)}</p>
                    <button className="cart-checkout" onClick={() => navigate("/checkout")}>Proceed to Checkout</button>
                </div>
            </div>
        </div>
    );
}

export default CartPage;
