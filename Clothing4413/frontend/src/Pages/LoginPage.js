import { useAuth } from "../Context/AuthContext";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../Styles/Auth.css";

function LoginPage() {
  const navigate = useNavigate();
  const { login, guestCart, clearGuestCart } = useAuth();

  const [form, setForm] = useState({
    email: "",
    password: ""
  });

  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      credentials: "include",
      body: JSON.stringify(form)
    });

    const data = await response.json();

    if (response.ok) {
      setMessage(`Welcome, ${data.name}`);
      login({
        id: data.id,
        name: data.name,
        email: data.email,
        userType: data.userType
      });

      if (data.userType === "CUSTOMER" && guestCart.length > 0) {
        await new Promise(res => setTimeout(res, 100));
        await mergeGuestCart(data.id);
      }

      navigate("/");
    } else {
      setMessage(data.message || "Login failed.");
    }
  };

  const mergeGuestCart = async (customerId) => {
    try {
      const response = await fetch(`http://localhost:8080/api/cart/${customerId}`);
      const cartData = await response.json();
      const cartItems = cartData.items || [];

      for (let i = 0; i < guestCart.length; i++) {
        const item = guestCart[i];
        try {
          //Get the current quantity of the item in the user's cart
          const existingItem = cartItems.find(
            c => c.product.id === item.product.product_id
          );

          //Check that when adding the guest cart quantity to the existing quantity, it does not exceed stock
          const existingQuantity = existingItem ? existingItem.quantity : 0;
          const stock = item.product.stock;
          const guestQuantity = item.quantity;

          const remainingStock = stock - existingQuantity;
          const quantityToAdd = remainingStock > 0 ? Math.min(guestQuantity, remainingStock) : 0;

          if (quantityToAdd <= 0) continue;

          await fetch("http://localhost:8080/api/cart/add", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({
              customerId: customerId,
              productId: item.product.product_id,
              quantity: quantityToAdd
            })
          });
        } catch (error) {
          console.error("Error merging guest cart item:", error);
        };
      }
      clearGuestCart();
    } catch (error) {
      console.error("Error merging guest cart:", error);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1>Login</h1>
        <p className="auth-subtitle">Sign in to your account</p>

        <form className="auth-form" onSubmit={handleSubmit}>
          <input
            type="email"
            name="email"
            placeholder="Email address"
            value={form.email}
            onChange={handleChange}
          />

          <input
            type="password"
            name="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
          />

          <button className="auth-btn" type="submit">Login</button>
        </form>

        {message && <div className="auth-message">{message}</div>}

        <div className="auth-footer">
          Don’t have an account? <Link to="/register">Register</Link>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;