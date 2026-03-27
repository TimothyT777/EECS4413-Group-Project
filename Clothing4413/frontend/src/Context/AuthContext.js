import { createContext, useContext, useState, useEffect } from "react";

//User is not logged in by default
const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // CHECK SESSION ON PAGE LOAD
  useEffect(() => {
    const checkSession = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/auth/me", {
                credentials: "include"
            });

            if (response.ok) {
                const data = await response.json();
                setUser({ id: data.id, name: data.name, email: data.email });
            }
        } catch (error) {
            console.error("Session check failed:", error);
        }
      };
      checkSession();
  }, []);

  const login = (userData) => setUser(userData);
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

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);