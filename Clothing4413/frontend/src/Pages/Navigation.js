import { Link } from "react-router-dom";
import { useAuth } from "../Context/AuthContext";
import "../Styles/HomePage.css";

function Navigation() {
  const { user } = useAuth();

  const isLoggedIn = !!user;
  const isAdmin = user?.userType === "ADMINISTRATOR";

  return (
    <div className="topnav">
      <Link className="active" to="/">Home</Link>

      {isAdmin && (
        <>
          <Link className="right" to="/admin/users">Admin Users</Link>
          <Link className="right" to="/admin/inventory">Admin Inventory</Link>
        </>
      )}

      <Link className="right" to="/cart">
        <img src="/img/cart.png" alt="cart" />Cart
      </Link>

      {!isLoggedIn && (
        <>
          <Link className="right" to="/register">Register</Link>
          <Link className="right" to="/login">Login</Link>
        </>
      )}

      {isLoggedIn && (
        <>
          <Link className="right" to="/user">Hello, {user.name}</Link>
        </>
      )}
    </div>
  );
}

export default Navigation;