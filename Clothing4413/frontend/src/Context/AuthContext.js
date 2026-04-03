import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [guestCart, setGuestCart] = useState([]); //Cart for users who are not logged in
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkSession = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/auth/me", {
          credentials: "include"
        });

        if (response.ok) {
          const data = await response.json();
          setUser({
            id: data.id,
            name: data.name,
            email: data.email,
            userType: data.userType
          });
        } else {
          setUser(null);
        }
      } catch (error) {
        console.error("Session check failed:", error);
        setUser(null);
      } finally {
        setLoading(false);
      }
    };

    checkSession();
  }, []);

  const login = (userData) => {
    setUser(userData);
  };

  const logout = async () => {
    try {
      await fetch("http://localhost:8080/api/auth/logout", {
        method: "POST",
        credentials: "include",
      });
    } catch (error) {
      console.error("Logout failed:", error);
    }
    setUser(null);
  };

  //Add an item to the guest cart.
  const addToGuestCart = (product) => {
    setGuestCart(prev => {
      for (let i = 0; i < prev.length; i++) {
        if (prev[i].product.product_id === product.product_id) {
          const updated = [...prev];
          updated[i] = { ...updated[i], quantity: updated[i].quantity + 1 };
          return updated;
        }
      }
      return [...prev, { product, quantity: 1 }];
    });
  };

  //Clear the guest cart
  const clearGuestCart = () => setGuestCart([]);

  return (
    <AuthContext.Provider value={{ user, login, logout, loading, clearGuestCart, guestCart, addToGuestCart }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);