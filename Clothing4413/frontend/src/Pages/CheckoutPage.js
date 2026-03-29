import { useState, useEffect } from "react";
import { useAuth } from "../Context/AuthContext";
import { useNavigate } from "react-router-dom";
import "../Styles/CheckoutPage.css";

function CheckoutPage() {
    const { user } = useAuth();
    const navigate = useNavigate();

    const [savedInfo, setSavedInfo] = useState(null);
    const [useSavedInfo, setUseSavedInfo] = useState(false);
    const [paymentDenied, setPaymentDenied] = useState(false);
    const [orderSummary, setOrderSummary] = useState(null);
    const [loading, setLoading] = useState(false);

    const [form, setForm] = useState({
        shippingAddress: "",
        billingAddress: "",
        cardHolderName: "",
        cardNumber: "",
        cardExpiry: "",
        saveInfo: false
    });

    useEffect(() => {
        if (!user) {
            navigate("/login");
            return;
        }
        const fetchSavedInfo = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/orders/customer-info/${user.id}`, {
                    credentials: "include"
                });
                const data = await response.json();
                if (data.hasSavedInfo) {
                    setSavedInfo(data);
                    setUseSavedInfo(true);
                    setForm({
                        shippingAddress: data.shippingAddress || "",
                        billingAddress: data.billingAddress || "",
                        cardHolderName: data.cardHolderName || "",
                        cardNumber: data.cardNumber || "",
                        cardExpiry: data.cardExpiry || "",
                        saveInfo: false
                    });
                }
            } catch (error) {
                console.error("Error fetching saved info:", error);
            }
        };
        fetchSavedInfo();
    }, [user]);

    const handleChange = (e) => {
        const value = e.target.type === "checkbox" ? e.target.checked : e.target.value;
        setForm({ ...form, [e.target.name]: value });
    };

    const handleUseSavedInfo = (e) => {
        setUseSavedInfo(e.target.checked);
        if (e.target.checked && savedInfo) {
            setForm({
                shippingAddress: savedInfo.shippingAddress || "",
                billingAddress: savedInfo.billingAddress || "",
                cardHolderName: savedInfo.cardHolderName || "",
                cardNumber: savedInfo.cardNumber || "",
                cardExpiry: savedInfo.cardExpiry || "",
                saveInfo: false
            });
        } else {
            setForm({
                shippingAddress: "",
                billingAddress: "",
                cardHolderName: "",
                cardNumber: "",
                cardExpiry: "",
                saveInfo: false
            });
        }
    };

    const handleSubmit = async () => {
        setLoading(true);
        setPaymentDenied(false);
        try {
            const response = await fetch("http://localhost:8080/api/orders/checkout", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({
                    customerId: user.id,
                    ...form
                })
            });
            const data = await response.json();
            if (response.ok) {
                setOrderSummary(data);
            } else if (data.message === "Credit Card Authorization Failed.") {
                setPaymentDenied(true);
            } else {
                alert(data.message || "Checkout failed.");
            }
        } catch (error) {
            console.error("Checkout error:", error);
        } finally {
            setLoading(false);
        }
    };

    // Sumamry Page on Order Confirmation
    if (orderSummary) {
        return (
            <div className="checkout-page">
                <div className="checkout-card">
                    <h1 className="checkout-success-title">Order Confirmed!</h1>
                    <p className="checkout-success-subtitle">Thank you for your purchase.</p>
                    <div className="summary-section">
                        <h2>Order #{orderSummary.id}</h2>
                        <p>Status: {orderSummary.status}</p>
                        {orderSummary.items.map((item) => (
                            <div className="summary-item" key={item.id}>
                                <img src={item.product.image} alt={item.product.name} className="summary-image"/>
                                <div className="summary-info">
                                    <p className="summary-name">{item.product.name}</p>
                                    <p className="summary-qty">Qty: {item.quantity}</p>
                                    <p className="summary-price">${item.priceAtPurchase} each</p>
                                </div>
                                <p className="summary-subtotal">${item.subtotal.toFixed(2)}</p>
                            </div>
                        ))}
                        <p className="summary-total">Total: ${orderSummary.total.toFixed(2)}</p>
                    </div>
                    <button className="checkout-btn" onClick={() => navigate("/")}>Continue Shopping</button>
                </div>
            </div>
        );
    }

    return (
        <div className="checkout-page">
            <div className="checkout-card">
                <h1 className="checkout-title">Checkout</h1>

                {/* Payment denied message */}
                {paymentDenied && (
                    <div className="checkout-denied">
                        <p>Credit Card Authorization Failed. Please try again or use a different card.</p>
                    </div>
                )}

                {/* Use saved info toggle */}
                {savedInfo && (
                    <div className="checkout-saved">
                        <label>
                            <input
                                type="checkbox"
                                checked={useSavedInfo}
                                onChange={handleUseSavedInfo}
                            />
                            Use saved billing and shipping information
                        </label>
                    </div>
                )}

                {/* Shipping */}
                <div className="checkout-section">
                    <h2>Shipping Information</h2>
                    <input
                        type="text"
                        name="shippingAddress"
                        placeholder="Shipping Address"
                        value={form.shippingAddress}
                        onChange={handleChange}
                        className="checkout-input"
                        disabled={useSavedInfo && !paymentDenied}
                    />
                </div>

                {/* Billing */}
                <div className="checkout-section">
                    <h2>Billing Information</h2>
                    <input
                        type="text"
                        name="billingAddress"
                        placeholder="Billing Address"
                        value={form.billingAddress}
                        onChange={handleChange}
                        className="checkout-input"
                        disabled={useSavedInfo && !paymentDenied}
                    />
                </div>

                {/* Payment */}
                <div className="checkout-section">
                    <h2>Payment Information</h2>
                    <input
                        type="text"
                        name="cardHolderName"
                        placeholder="Cardholder Name"
                        value={form.cardHolderName}
                        onChange={handleChange}
                        className="checkout-input"
                        disabled={useSavedInfo && !paymentDenied}
                    />
                    <input
                        type="text"
                        name="cardNumber"
                        placeholder="Card Number (16 digits)"
                        value={form.cardNumber}
                        onChange={handleChange}
                        className="checkout-input"
                        maxLength={16}
                        disabled={useSavedInfo && !paymentDenied}
                    />
                    <input
                        type="text"
                        name="cardExpiry"
                        placeholder="Expiry Date (MM/YY)"
                        value={form.cardExpiry}
                        onChange={handleChange}
                        className="checkout-input"
                        disabled={useSavedInfo && !paymentDenied}
                    />
                </div>

                {/* Save info checkbox — only show if no saved info exists */}
                {!savedInfo && (
                    <div className="checkout-save">
                        <label>
                            <input
                                type="checkbox"
                                name="saveInfo"
                                checked={form.saveInfo}
                                onChange={handleChange}
                            />
                            Save billing and shipping information for future orders
                        </label>
                    </div>
                )}

                <button
                    className="checkout-btn"
                    onClick={handleSubmit}
                    disabled={loading}
                >
                    {loading ? "Processing..." : "Confirm Order"}
                </button>

                <button
                    className="checkout-cancel"
                    onClick={() => navigate("/cart")}
                >
                    Back to Cart
                </button>
            </div>
        </div>
    );
}

export default CheckoutPage;