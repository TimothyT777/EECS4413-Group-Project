import { useEffect, useRef, useState } from "react";
import "../Styles/AdminInventory.css";

const PRODUCTS_URL = "http://localhost:8080/api/products";
const ADMIN_INVENTORY_URL = "http://localhost:8080/api/products/admin/inventory";

function AdminInventoryPage() {
  const [products, setProducts] = useState([]);
  const [message, setMessage] = useState("");
  const fileInputRef = useRef(null);

  const [newProduct, setNewProduct] = useState({
    name: "",
    description: "",
    price: "",
    quantity: "",
    brand:"",
    category: "",
    image: null
  });

  const [quantityUpdates, setQuantityUpdates] = useState({});
  const [categories, setCategories] = useState([]);
  const [productToDelete, setProductToDelete] = useState(null);

  useEffect(() => {
    fetch("/api/categories")
      .then((res) => res.json())
      .then((data) => setCategories(data));
}, []);

  const fetchProducts = async () => {
    try {
      setMessage("");
      const response = await fetch(PRODUCTS_URL, {
        credentials: "include"
      });

      const data = await response.json();

      if (!response.ok) {
        setProducts([]);
        setMessage(data.message || "Failed to load products.");
        return;
      }

      setProducts(data);
    } catch (error) {
      setProducts([]);
      setMessage("Failed to fetch products.");
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleNewProductChange = (e) => {
    const { name, value, files } = e.target;

    if (name === "image") {
      setNewProduct({
        ...newProduct,
        image: files && files[0] ? files[0] : null
      });
      return;
    }

    setNewProduct({
      ...newProduct,
      [name]: value
    });
  };

  const handleAddProduct = async (e) => {
    e.preventDefault();

    const parsedPrice = parseFloat(newProduct.price);
    const parsedQuantity = parseInt(newProduct.quantity, 10);

    if (!newProduct.name.trim()) {
      setMessage("Product name is required.");
      return;
    }

    if (Number.isNaN(parsedPrice) || parsedPrice < 0) {
      setMessage("Enter a valid price.");
      return;
    }

    if (Number.isNaN(parsedQuantity) || parsedQuantity < 0) {
      setMessage("Enter a valid quantity.");
      return;
    }

    if (!newProduct.image) {
      setMessage("Please upload a product image.");
      return;
    }

    if (!newProduct.brand.trim()) {
      setMessage("Product brand is required.");
      return;
    }

    try {
      const formData = new FormData();
      formData.append("name", newProduct.name.trim());
      formData.append("description", newProduct.description.trim());
      formData.append("price", parsedPrice);
      formData.append("quantity", parsedQuantity);
      formData.append("brand", newProduct.brand.trim());
      formData.append("category", newProduct.category);
      formData.append("image", newProduct.image);

      const response = await fetch(ADMIN_INVENTORY_URL, {
        method: "POST",
        credentials: "include",
        body: formData
      });

      const data = await response.json();

      if (response.ok) {
        setMessage(`Added product: ${data.name}`);
        setNewProduct({
          name: "",
          description: "",
          price: "",
          quantity: "",
          brand: "",
          category: "",
          image: null
        });

        if (fileInputRef.current) {
          fileInputRef.current.value = "";
        }

        fetchProducts();
      } else {
        setMessage(data.message || "Failed to add product.");
      }
    } catch (error) {
      setMessage("Failed to add product.");
    }
  };

  const handleQuantityChange = (id, value) => {
    setQuantityUpdates({
      ...quantityUpdates,
      [id]: value
    });
  };

  const handleUpdateQuantity = async (id) => {
    const parsedQuantity = parseInt(quantityUpdates[id], 10);

    if (Number.isNaN(parsedQuantity) || parsedQuantity < 0) {
      setMessage("Enter a valid quantity.");
      return;
    }

    try {
      const response = await fetch(`${ADMIN_INVENTORY_URL}/${id}/quantity`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({
          quantity: parsedQuantity
        })
      });

      const data = await response.json();

      if (response.ok) {
        setMessage(`Updated quantity for ${data.name}`);
        setQuantityUpdates({
          ...quantityUpdates,
          [id]: ""
        });
        fetchProducts();
      } else {
        setMessage(data.message || "Failed to update quantity.");
      }
    } catch (error) {
      setMessage("Failed to update quantity.");
    }
  };

  const handleDelete = async (id) =>{

    try{
      const response = await fetch(`${ADMIN_INVENTORY_URL}/${id}`, {
        method: "DELETE",
      });

      const data = await response.json();

      if (response.ok) {
        setMessage(`Deleted product: ${id}`);
        fetchProducts();
        setProductToDelete(null);
      }else {
        setMessage(data.message || "Failed to delete product.");
      }

    }catch (error){
      setMessage("Failed to delete product.");
    }
  }

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

            <form className="admin-form" onSubmit={handleAddProduct} encType="multipart/form-data">
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

              <input
                type="text"
                name="brand"
                placeholder="Brand"
                value={newProduct.brand}
                onChange={handleNewProductChange}
              />

              <select
                name="category"
                value={newProduct.category||""}
                onChange={handleNewProductChange}
              >
                <option value="" disabled>
                  Choose a category
                </option>
                {categories.map((cat) => (
                  <option key={cat} value={cat}>
                    {cat}
                  </option>
                ))}
              </select>

              <input
                ref={fileInputRef}
                type="file"
                name="image"
                accept="image/*"
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
                      <div className="inventory-product-info">
                        <img
                          src={product.image}
                          alt={product.name}
                          className="inventory-image"
                        />

                        <div>
                          <h3>{product.name}</h3>
                          <p className="inventory-description">{product.description}</p>
                        </div>
                      </div>
                    </div>

                    <div className="inventory-meta">
                      <div className="inventory-badge">Price: ${Number(product.price).toFixed(2)}</div>
                      <div className="inventory-badge">
                        Quantity: {product.stock ?? product.quantity ?? 0}
                      </div>
                      <div className="inventory-badge">Brand: {product.brand}</div>
                      <div className="inventory-badge">Category: {product.category}</div>
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
                      <button className="delete-btn"
                        onClick={() => setProductToDelete(product)}
                      >
                        Delete Product
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      </div>
      {productToDelete && (
      <div className="delete-modal" onClick={() => setProductToDelete(null)}>
        <div className="inventory-item" onClick={(e) => e.stopPropagation()}>
          
          <h3>Delete Product</h3>
          <p>Are you sure you want to delete <strong>{productToDelete.name}</strong>?</p>

          <div className="inventory-update">
            <button
              className="admin-btn"
              onClick={() => setProductToDelete(null)}
            >
              Cancel
            </button>

            <button
              className="delete-btn"
              onClick={() => handleDelete(productToDelete.product_id)}
            >
              Delete
            </button>
          </div>

        </div>
      </div>
    )}
    </div>
  );
}

export default AdminInventoryPage;