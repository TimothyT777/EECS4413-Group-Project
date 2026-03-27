import { Link, useNavigate } from "react-router-dom";
import "../Styles/HomePage.css";

function Navigation() {
  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem("user"));
  const isLoggedIn = !!user;
  const isAdmin = user?.userType === "ADMINISTRATOR";

  const handleLogout = () => {
    localStorage.removeItem("user");
    navigate("/");
    window.location.reload();
  };

  return (
    <div className="topnav">
      <Link className="active" to="/">Home</Link>

      {isAdmin && (
        <>
          <Link className="right" to="/admin/users">Admin Users</Link>
          <Link className="right" to="/admin/inventory">Admin Inventory</Link>
        </>
      )}

      {!isLoggedIn && (
        <>
          <Link className="right" to="/register">Register</Link>
          <Link className="right" to="/login">Login</Link>
        </>
      )}

      {isLoggedIn && (
        <button type="button" className="nav-logout" onClick={handleLogout}>
        Logout
        </button>
      )}

      <Link className="right" to="/cart">
        <img src="/img/cart.png" alt="cart" />Cart
      </Link>
    </div>
  );
}

export default Navigation;