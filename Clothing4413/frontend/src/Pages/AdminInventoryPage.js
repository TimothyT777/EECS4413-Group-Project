import { useEffect, useState } from "react";
import "../Styles/AdminInventory.css";

function AdminInventoryPage() {
  const [products, setProducts] = useState([]);
  const [message, setMessage] = useState("");

  const [newProduct, setNewProduct] = useState({
    name: "",
    description: "",
    price: "",
    quantity: ""
  });

  const [quantityUpdates, setQuantityUpdates] = useState({});

  const fetchProducts = async () => {
    const response = await fetch("http://localhost:8080/api/products");
    const data = await response.json();
    setProducts(data);
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleNewProductChange = (e) => {
    setNewProduct({
      ...newProduct,
      [e.target.name]: e.target.value
    });
  };

  const handleAddProduct = async (e) => {
    e.preventDefault();

    const response = await fetch("http://localhost:8080/api/admin/inventory", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        name: newProduct.name,
        description: newProduct.description,
        price: parseFloat(newProduct.price),
        quantity: parseInt(newProduct.quantity)
      })
    });

    const data = await response.json();

    if (response.ok) {
      setMessage(`Added product: ${data.name}`);
      setNewProduct({
        name: "",
        description: "",
        price: "",
        quantity: ""
      });
      fetchProducts();
    } else {
      setMessage(data.message || "Failed to add product.");
    }
  };

  const handleQuantityChange = (id, value) => {
    setQuantityUpdates({
      ...quantityUpdates,
      [id]: value
    });
  };

  const handleUpdateQuantity = async (id) => {
    const response = await fetch(`http://localhost:8080/api/admin/inventory/${id}/quantity`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        quantity: parseInt(quantityUpdates[id])
      })
    });

    const data = await response.json();

    if (response.ok) {
      setMessage(`Updated quantity for ${data.name}`);
      fetchProducts();
    } else {
      setMessage(data.message || "Failed to update quantity.");
    }
  };

  return (
    <div className="admin-page">
      <div className="admin-container">
        <div className="admin-header">
          <h1>Admin Inventory</h1>
          <p>Manage products and update stock levels.</p>
        </div>

        {message && <div className="admin-message">{message}</div>}

        <div className="admin-grid">
          <div className="admin-card">
            <h2>Add Product</h2>

            <form className="admin-form" onSubmit={handleAddProduct}>
              <input
                type="text"
                name="name"
                placeholder="Product name"
                value={newProduct.name}
                onChange={handleNewProductChange}
              />

              <input
                type="text"
                name="description"
                placeholder="Description"
                value={newProduct.description}
                onChange={handleNewProductChange}
              />

              <input
                type="number"
                step="0.01"
                name="price"
                placeholder="Price"
                value={newProduct.price}
                onChange={handleNewProductChange}
              />

              <input
                type="number"
                name="quantity"
                placeholder="Quantity"
                value={newProduct.quantity}
                onChange={handleNewProductChange}
              />

              <button className="admin-btn" type="submit">
                Add Product
              </button>
            </form>
          </div>

          <div className="admin-card">
            <h2>Current Inventory</h2>

            <div className="inventory-list">
              {products.length === 0 ? (
                <p className="empty-text">No products found.</p>
              ) : (
                products.map((product) => (
                  <div key={product.product_id} className="inventory-item">
                    <div className="inventory-top">
                      <div>
                        <h3>{product.name}</h3>
                        <p className="inventory-description">{product.description}</p>
                      </div>
                    </div>

                    <div className="inventory-meta">
                      <div className="inventory-badge">Price: ${product.price}</div>
                      <div className="inventory-badge">Quantity: {product.quantity}</div>
                      <div className="inventory-badge">ID: {product.product_id}</div>
                    </div>

                    <div className="inventory-update">
                      <input
                        type="number"
                        placeholder="New quantity"
                        value={quantityUpdates[product.product_id] || ""}
                        onChange={(e) =>
                          handleQuantityChange(product.product_id, e.target.value)
                        }
                      />
                      <button
                        className="admin-btn"
                        onClick={() => handleUpdateQuantity(product.product_id)}
                      >
                        Update Quantity
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminInventoryPage;