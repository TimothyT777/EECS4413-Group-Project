import { useEffect, useState } from "react";
import "../Styles/AdminUsers.css";

const USERS_URL = "http://localhost:8080/api/admin/users";

function AdminUsersPage() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [loadingDetails, setLoadingDetails] = useState(false);
  const [message, setMessage] = useState("");

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    shippingAddress: "",
    billingAddress: "",
    cardHolderName: "",
    cardNumber: "",
    cardExpiry: ""
  });

  const fetchUsers = async () => {
    try {
      setMessage("");
      const response = await fetch(USERS_URL, {
        credentials: "include"
      });
      const data = await response.json();

      if (response.ok) {
        setUsers(data);
      } else {
        setMessage(data.message || "Failed to load users.");
      }
    } catch (error) {
      setMessage("Failed to connect to server.");
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const loadUserDetails = async (id) => {
    try {
      setLoadingDetails(true);
      setMessage("");

      const response = await fetch(`${USERS_URL}/${id}`, {
        credentials: "include"
      });
      const data = await response.json();

      if (response.ok) {
        setSelectedUser(data);
        setForm({
          name: data.name || "",
          email: data.email || "",
          password: "",
          shippingAddress: data.shippingAddress || "",
          billingAddress: data.billingAddress || "",
          cardHolderName: data.cardHolderName || "",
          cardNumber: data.cardNumber || "",
          cardExpiry: data.cardExpiry || ""
        });
      } else {
        setMessage(data.message || "Failed to load user details.");
      }
    } catch (error) {
      setMessage("Failed to connect to server.");
    } finally {
      setLoadingDetails(false);
    }
  };

  const handleSelectUser = (user) => {
    loadUserDetails(user.id);
  };

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleUpdate = async (e) => {
    e.preventDefault();

    if (!selectedUser) return;

    try {
      const response = await fetch(`${USERS_URL}/${selectedUser.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify(form)
      });

      const data = await response.json();

      if (response.ok) {
        setMessage("User updated successfully.");
        setSelectedUser(data);

        setUsers((prevUsers) =>
          prevUsers.map((user) =>
            user.id === data.id
              ? {
                  ...user,
                  name: data.name,
                  email: data.email,
                  userType: data.userType
                }
              : user
          )
        );

        setForm({
          name: data.name || "",
          email: data.email || "",
          password: "",
          shippingAddress: data.shippingAddress || "",
          billingAddress: data.billingAddress || "",
          cardHolderName: data.cardHolderName || "",
          cardNumber: data.cardNumber || "",
          cardExpiry: data.cardExpiry || ""
        });
      } else {
        setMessage(data.message || "Failed to update user.");
      }
    } catch (error) {
      setMessage("Failed to connect to server.");
    }
  };

  return (
    <div className="admin-users-page">
      <h1>Admin User Management</h1>
      <p className="admin-users-subtitle">
        View and update user accounts, customer info, and purchase history.
      </p>

      {message && <div className="admin-users-global-message">{message}</div>}

      <div className="admin-users-layout">
        <div className="admin-users-list">
          <h2>Users</h2>

          {users.length === 0 ? (
            <p>No users found.</p>
          ) : (
            <table className="admin-users-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Type</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>{user.name}</td>
                    <td>{user.email}</td>
                    <td>{user.userType}</td>
                    <td>
                      <button onClick={() => handleSelectUser(user)}>Edit</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        <div className="admin-users-form-card">
          <h2>Edit User</h2>

          {!selectedUser ? (
            <p>Select a user to edit.</p>
          ) : loadingDetails ? (
            <p>Loading user details...</p>
          ) : (
            <>
              <form className="admin-users-form" onSubmit={handleUpdate}>
                <input
                  type="text"
                  name="name"
                  placeholder="Name"
                  value={form.name}
                  onChange={handleChange}
                />

                <input
                  type="email"
                  name="email"
                  placeholder="Email"
                  value={form.email}
                  onChange={handleChange}
                />

                <input
                  type="password"
                  name="password"
                  placeholder="New password (leave blank to keep current)"
                  value={form.password}
                  onChange={handleChange}
                />

                {selectedUser.userType === "CUSTOMER" && (
                  <>
                    <input
                      type="text"
                      name="shippingAddress"
                      placeholder="Shipping address"
                      value={form.shippingAddress}
                      onChange={handleChange}
                    />

                    <input
                      type="text"
                      name="billingAddress"
                      placeholder="Billing address"
                      value={form.billingAddress}
                      onChange={handleChange}
                    />

                    <input
                      type="text"
                      name="cardHolderName"
                      placeholder="Card holder name"
                      value={form.cardHolderName}
                      onChange={handleChange}
                    />

                    <input
                      type="text"
                      name="cardNumber"
                      placeholder="Card number"
                      value={form.cardNumber}
                      onChange={handleChange}
                    />

                    <input
                      type="text"
                      name="cardExpiry"
                      placeholder="Card expiry (MM/YY)"
                      value={form.cardExpiry}
                      onChange={handleChange}
                    />
                  </>
                )}

                <button type="submit">Update User</button>
              </form>

              {selectedUser.userType === "CUSTOMER" && (
                <div className="admin-user-orders">
                  <h3>Purchase History</h3>

                  {!selectedUser.orders || selectedUser.orders.length === 0 ? (
                    <p>No purchase history found.</p>
                  ) : (
                    selectedUser.orders.map((order) => (
                      <div className="admin-order-card" key={order.id}>
                        <div className="admin-order-header">
                          <div>
                            <strong>Order #{order.id}</strong>
                          </div>
                          <div>
                            {order.createdAt ? new Date(order.createdAt).toLocaleString() : ""}
                          </div>
                        </div>

                        <div className="admin-order-total">
                          Total: ${Number(order.total || 0).toFixed(2)}
                        </div>

                        <table className="admin-order-items-table">
                          <thead>
                            <tr>
                              <th>Product</th>
                              <th>Qty</th>
                              <th>Price</th>
                              <th>Subtotal</th>
                            </tr>
                          </thead>
                          <tbody>
                            {order.items?.map((item) => (
                              <tr key={item.id}>
                                <td>{item.product?.name}</td>
                                <td>{item.quantity}</td>
                                <td>${Number(item.priceAtPurchase).toFixed(2)}</td>
                                <td>${Number(item.subtotal).toFixed(2)}</td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </div>
                    ))
                  )}
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export default AdminUsersPage;