import { useEffect, useState } from "react";
import "../Styles/AdminUsers.css";

function AdminUsersPage() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: ""
  });
  const [message, setMessage] = useState("");

  const fetchUsers = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/admin/users");
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

  const handleSelectUser = (user) => {
    setSelectedUser(user);
    setForm({
      name: user.name || "",
      email: user.email || "",
      password: ""
    });
    setMessage("");
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
      const response = await fetch(`http://localhost:8080/api/admin/users/${selectedUser.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(form)
      });

      const data = await response.json();

      if (response.ok) {
        setMessage("User updated successfully.");

        const updatedUsers = users.map((user) =>
          user.id === data.id ? data : user
        );

        setUsers(updatedUsers);
        setSelectedUser(data);
        setForm({
          name: data.name || "",
          email: data.email || "",
          password: ""
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
      <p className="admin-users-subtitle">View and update user accounts</p>

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
          ) : (
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

              <button type="submit">Update User</button>
            </form>
          )}

          {message && <div className="admin-users-message">{message}</div>}
        </div>
      </div>
    </div>
  );
}

export default AdminUsersPage;