import './App.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import HomePage from './Pages/HomePage';
import Navigation from './Pages/Navigation';
import LoginPage from './Pages/LoginPage';
import RegisterPage from './Pages/RegisterPage';
import CartPage from './Pages/CartPage';
import CheckoutPage from './Pages/CheckoutPage';
import AdminInventoryPage from './Pages/AdminInventoryPage';
import AdminUsersPage from './Pages/AdminUsersPage';
import AdminSalesHistoryPage from './Pages/AdminSalesHistoryPage';
import { useAuth } from "./Context/AuthContext";
import UserPage from './Pages/UserPage';

function ProtectedAdminRoute({ children }) {
  const { user, loading } = useAuth();

  if (loading) {
    return <div>Loading...</div>;
  }

  const isAdmin = user?.userType === "ADMINISTRATOR";
  return isAdmin ? children : <Navigate to="/" replace />;
}

function ProtectedCustomerRoute({ children }) {
  const { user, loading } = useAuth();

  if (loading) {
    return <div>Loading...</div>;
  }

  const isCustomer = user?.userType === "CUSTOMER";
  return isCustomer ? children : <Navigate to="/" replace />;
}

function App() {
  return (
    <Router>
      <Navigation />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/cart"
          element={
            <ProtectedCustomerRoute>
              <CartPage />
            </ProtectedCustomerRoute>
          }
        />

        <Route
          path="/checkout"
          element={
            <ProtectedCustomerRoute>
              <CheckoutPage />
            </ProtectedCustomerRoute>
          }
        />
        <Route path="/user" element={<UserPage />} />

        <Route
          path="/admin/inventory"
          element={
            <ProtectedAdminRoute>
              <AdminInventoryPage />
            </ProtectedAdminRoute>
          }
        />

        <Route
          path="/admin/users"
          element={
            <ProtectedAdminRoute>
              <AdminUsersPage />
            </ProtectedAdminRoute>
          }
        />

        <Route
          path="/admin/sales-history"
          element={
            <ProtectedAdminRoute>
              <AdminSalesHistoryPage />
            </ProtectedAdminRoute>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;