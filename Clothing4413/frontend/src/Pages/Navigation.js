import { Link } from "react-router-dom";
import { useAuth } from "../Context/AuthContext";
import '../Styles/HomePage.css';

function Navigation() {
    const { user } = useAuth();

    return (
        <div className="topnav">
            <Link className="active" to="/">Home</Link>
            <Link className="right" to="/cart">
                <img src="/img/cart.png" alt="cart" />Cart
            </Link>
            {/* Show User Account link if logged in, otherwise show Login and Register */}
            {user ? (
                <Link className="right" to="/user">Hello, {user.name}</Link>
            ) : (
                <>
                    <Link className="right" to="/register">Register</Link>
                    <Link className="right" to="/login">Login</Link>
                </>
            )}
        </div>
    );
}

export default Navigation;