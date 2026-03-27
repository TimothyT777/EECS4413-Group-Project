import { Link } from "react-router-dom";
import '../Styles/HomePage.css';

function Navigation() {
  return (
    <div className="topnav">
      <Link className="active" to="/">Home</Link>
      <Link className="right" to="/register">Register</Link>
      <Link className="right" to="/login">Login</Link>
      <Link className="right" to="/cart">
        <img src="/img/cart.png" alt="cart" />Cart
      </Link>
    </div>
  );
}

export default Navigation;