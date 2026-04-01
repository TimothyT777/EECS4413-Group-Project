import { useState, useEffect } from "react";
import { useAuth } from "../Context/AuthContext";
import { useNavigate } from "react-router-dom";
import "../Styles/UserPage.css";

function UserPage() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const [savedInfo, setSavedInfo] = useState(null);
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // This should never happen since the userpage button is hidden untill you are logged in, but just in case
        if (!user) {
            navigate("/login");
            return;
        }

        const fetchData = async () => {
            try {
                // Fetch saved billing/shipping info
                const infoResponse = await fetch(`http://localhost:8080/api/orders/customer-info/${user.id}`, {
                    credentials: "include"
                });
                const infoData = await infoResponse.json();
                if (infoData.hasSavedInfo) {
                    setSavedInfo(infoData);
                }

                // Fetch order history
                const ordersResponse = await fetch(`http://localhost:8080/api/orders/${user.id}`, {
                    credentials: "include"
                });
                const ordersData = await ordersResponse.json();
                setOrders(ordersData);
            } catch (error) {
                console.error("Error fetching user data:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [user]);

    const handleLogout = async () => {
        await fetch("http://localhost:8080/api/auth/logout", {
            method: "POST",
            credentials: "include"
        });
        logout();
        navigate("/");
    };

    if (!user) return null;

    if (loading) {
        return (
            <div className="user-page">
                <p className="orders-message">Loading...</p>
            </div>
        );
    }

    return (
        <div className="user-page">
            <div className="user-container">

                {/* Account Information */}
                <div className="user-info-card">
                    <h1 className="user-title">My Account</h1>
                    <div className="user-details">
                        <p><span className="user-label">Name:</span> {user.name}</p>
                        <p><span className="user-label">Email:</span> {user.email}</p>
                    </div>

                    {/* Saved Billing and Shipping — only shown if it exists */}
                    {savedInfo && (
                        <div className="user-saved-info">
                            <h2 className="user-saved-title">Saved Information</h2>
                            <div className="user-details">
                                <p><span className="user-label">Shipping Address:</span> {savedInfo.shippingAddress}</p>
                                <p><span className="user-label">Billing Address:</span> {savedInfo.billingAddress}</p>
                                <p><span className="user-label">Cardholder Name:</span> {savedInfo.cardHolderName}</p>
                                <p><span className="user-label">Card Number:</span> **** **** **** {savedInfo.cardNumber.slice(-4)}</p>
                                <p><span className="user-label">Card Expiry:</span> {savedInfo.cardExpiry}</p>
                            </div>
                        </div>
                    )}

                    <button className="user-logout" onClick={handleLogout}>Logout</button>
                </div>

                {/* Order History */}
                <div className="user-orders">
                    <h2 className="orders-title">Order History</h2>
                    {orders.length === 0 ? (
                        <p className="orders-message">You have no orders yet.</p>
                    ) : (
                        orders.map((order) => (
                            <div className="order-card" key={order.id}>
                                <div className="order-header">
                                    <p className="order-id">Order #{order.id}</p>
                                    <p className="order-date">{new Date(order.createdAt).toLocaleDateString()}</p>
                                </div>
                                <div className="order-items">
                                    {order.items.map((item) => (
                                        <div className="order-item" key={item.id}>
                                            <img src={item.product.image} alt={item.product.name} className="order-item-image"/>
                                            <div className="order-item-info">
                                                <p className="order-item-name">{item.product.name}</p>
                                                <p className="order-item-brand">{item.product.brand}</p>
                                                <p className="order-item-quantity">Qty: {item.quantity}</p>
                                                <p className="order-item-price">${item.priceAtPurchase} each</p>
                                            </div>
                                            <p className="order-item-subtotal">${item.subtotal.toFixed(2)}</p>
                                        </div>
                                    ))}
                                </div>
                                <div className="order-footer">
                                    <p className="order-total">Total: ${order.total.toFixed(2)}</p>
                                </div>
                            </div>
                        ))
                    )}
                </div>

            </div>
        </div>
    );
}

export default UserPage;