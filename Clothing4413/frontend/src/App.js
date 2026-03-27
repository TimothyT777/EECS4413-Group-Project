import './App.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import HomePage from './Pages/HomePage';
import Navigation from './Pages/Navigation';
import LoginPage from './Pages/LoginPage';
import RegisterPage from './Pages/RegisterPage';
import CartPage from './Pages/CartPage';
import AdminInventoryPage from './Pages/AdminInventoryPage';
import AdminUsersPage from './Pages/AdminUsersPage';

function ProtectedAdminRoute({ children }) {
  const user = JSON.parse(localStorage.getItem("user"));
  const isAdmin = user?.userType === "ADMINISTRATOR";

  return isAdmin ? children : <Navigate to="/" replace />;
}

function App() {
  return (
    <Router>
      <Navigation />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/cart" element={<CartPage />} />
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
      </Routes>
    </Router>
  );
}

export default App;