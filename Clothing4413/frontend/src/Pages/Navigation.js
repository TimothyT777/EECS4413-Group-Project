import { Link } from "react-router-dom";
import '../Styles/HomePage.css';

function Navigation(){
	return (
		<div className="topnav">
	      <Link className="active" to="/">Home</Link>
	      <Link to="/news">News</Link>
	      <Link to="/contact">Contact</Link>
	      <Link to="/about">About</Link>
	      <Link className="right" to="/login">Login</Link>
	      <Link className="right" to="/cart">
	      <img src="/img/cart.png" alt="cart" />Cart</Link>
	    </div>
	);
}

export default Navigation;