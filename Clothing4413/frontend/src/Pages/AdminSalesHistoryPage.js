import { useEffect, useState } from "react";
import "../Styles/AdminSalesHistory.css";

function AdminSalesHistoryPage() {
  const [orders, setOrders] = useState([]);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true);

  const [filters, setFilters] = useState({
    customerId: "",
    productId: "",
    startDate: "",
    endDate: ""
  });

  const fetchSalesHistory = async (activeFilters = filters) => {
    try {
      setLoading(true);
      setMessage("");

      const params = new URLSearchParams();

      if (activeFilters.customerId) params.append("customerId", activeFilters.customerId);
      if (activeFilters.productId) params.append("productId", activeFilters.productId);
      if (activeFilters.startDate) params.append("startDate", activeFilters.startDate);
      if (activeFilters.endDate) params.append("endDate", activeFilters.endDate);

      const response = await fetch(
        `http://localhost:8080/api/admin/sales-history?${params.toString()}`,
        {
          credentials: "include"
        }
      );

      const data = await response.json();

      if (response.ok) {
        setOrders(data);
      } else {
        setOrders([]);
        setMessage(data.message || "Failed to load sales history.");
      }
    } catch (error) {
      setOrders([]);
      setMessage("Failed to connect to server.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSalesHistory();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleChange = (e) => {
    setFilters({
      ...filters,
      [e.target.name]: e.target.value
    });
  };

  const handleFilterSubmit = (e) => {
    e.preventDefault();
    fetchSalesHistory(filters);
  };

  const handleClearFilters = () => {
    const cleared = {
      customerId: "",
      productId: "",
      startDate: "",
      endDate: ""
    };
    setFilters(cleared);
    fetchSalesHistory(cleared);
  };

  return (
    <div className="admin-sales-page">
      <div className="admin-sales-container">
        <div className="admin-sales-header">
          <h1>Admin Sales History</h1>
          <p>View all sales orders and filter by customer, product, or date.</p>
        </div>

        <div className="admin-sales-card">
          <h2>Filters</h2>

          <form className="admin-sales-filters" onSubmit={handleFilterSubmit}>
            <input
              type="number"
              name="customerId"
              placeholder="Customer ID"
              value={filters.customerId}
              onChange={handleChange}
            />

            <input
              type="number"
              name="productId"
              placeholder="Product ID"
              value={filters.productId}
              onChange={handleChange}
            />

            <input
              type="date"
              name="startDate"
              value={filters.startDate}
              onChange={handleChange}
            />

            <input
              type="date"
              name="endDate"
              value={filters.endDate}
              onChange={handleChange}
            />

            <div className="admin-sales-filter-actions">
              <button type="submit">Apply Filters</button>
              <button type="button" className="secondary-btn" onClick={handleClearFilters}>
                Clear
              </button>
            </div>
          </form>
        </div>

        {message && <div className="admin-sales-message">{message}</div>}

        <div className="admin-sales-card">
          <h2>Orders</h2>

          {loading ? (
            <p className="empty-text">Loading sales history...</p>
          ) : orders.length === 0 ? (
            <p className="empty-text">No sales orders found.</p>
          ) : (
            <div className="sales-orders-list">
              {orders.map((order) => (
                <div className="sales-order-card" key={order.id}>
                  <div className="sales-order-header">
                    <div>
                      <h3>Order #{order.id}</h3>
                      <p>
                        Customer: {order.customerName} (ID: {order.customerId})
                      </p>
                      <p>Email: {order.customerEmail}</p>
                    </div>
                    <div className="sales-order-header-right">
                      <p>{new Date(order.createdAt).toLocaleString()}</p>
                      <p className="sales-order-total">
                        Total: ${Number(order.total).toFixed(2)}
                      </p>
                    </div>
                  </div>

                  <div className="sales-order-items">
                    <table>
                      <thead>
                        <tr>
                          <th>Product ID</th>
                          <th>Product</th>
                          <th>Brand</th>
                          <th>Qty</th>
                          <th>Price</th>
                          <th>Subtotal</th>
                        </tr>
                      </thead>
                      <tbody>
                        {order.items.map((item) => (
                          <tr key={item.orderItemId}>
                            <td>{item.productId}</td>
                            <td>{item.productName}</td>
                            <td>{item.productBrand || "N/A"}</td>
                            <td>{item.quantity}</td>
                            <td>${Number(item.priceAtPurchase).toFixed(2)}</td>
                            <td>${Number(item.subtotal).toFixed(2)}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default AdminSalesHistoryPage;