import '../Styles/HomePage.css';
import { useState, useEffect, useMemo } from "react";
import { useAuth } from "../Context/AuthContext";
import '../index.css';

function HomePage() {
	const [searchTerm, setSearchTerm] = useState("");
	const [debouncedSearch, setDebouncedSearch] = useState("");
	const [selectedProduct, setSelectedProduct] = useState(null);
	const [selectedBrand, setSelectedBrand] = useState("");
	const [selectedCategory, setSelectedCategory] = useState("");
	const [sortOrder, setSortOrder] = useState("");
	const [minPrice, setMinPrice] = useState(null);
	const [maxPrice, setMaxPrice] = useState(null);
	const [products, setProducts] = useState([]);

	const { user } = useAuth();

	// Gets all the products from the database.
	const brands = useMemo(() => [...new Set(products.map(p => p.brand))], [products]);
  	const category = useMemo(() => [...new Set(products.map(p => p.category))], [products]);
	useEffect(() => {
		const fetchProducts = async () => {
			try {
				const response = await fetch("http://localhost:8080/api/products");
				const data = await response.json();
				setProducts(data);
			} catch (error) {
				console.error("Error fetching products:", error);
			}
		};
		fetchProducts();
	}, []);

	// Debouncing for search.
	useEffect(() => {
		const handler = setTimeout(() => {
		setDebouncedSearch(searchTerm);
		}, 300);
			return () => clearTimeout(handler);
	}, [searchTerm]);

	// Advanced filters combing search term, category, brand, and price range.
	const filteredProduct = useMemo(() => {
		const term = debouncedSearch.toLowerCase(); // only convert once per search
		const min = minPrice != null ? minPrice : -Infinity;
  		const max = maxPrice != null ? maxPrice : Infinity;
		return products.filter(p => {
			const matchedTerm = term ? p.name.toLowerCase().includes(term) : true;
			const matchedCategory = selectedCategory ? p.category === selectedCategory : true;
			const matchedBrand = selectedBrand ? p.brand === selectedBrand : true;
			const matchedMinPrice = minPrice != null ? Number(p.price) >= minPrice : true;
			const matchedMaxPrice = maxPrice != null ? Number(p.price) <= maxPrice : true;
			return matchedTerm && matchedCategory && matchedBrand && matchedMinPrice && matchedMaxPrice;
		}).sort((a, b) => {
			if (sortOrder === "asc") return a.name.localeCompare(b.name);
			if (sortOrder === "desc") return b.name.localeCompare(a.name);
			if (sortOrder === "price-asc") return a.price - b.price;
			if (sortOrder === "price-desc") return b.price - a.price;
			return 0;
		});
 	}, [products, debouncedSearch, selectedCategory, selectedBrand, minPrice, maxPrice, sortOrder]);



	// Handles clicking on catalogue items.
	const handleItemClick = (products) => setSelectedProduct(products);
	const handleClose = () => setSelectedProduct(null);

	//Handles adding an item to the cart
	const handleAddToCart = async (product) => {
		if (!user) {
			alert("Please log in to add items to your cart.");
			return;
		}

		try {
			const response = await fetch("http://localhost:8080/api/cart/add", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				credentials: "include",
				body: JSON.stringify({
					customerId: user.id,
					productId: product.product_id,
					quantity: 1
				})
			});

			if (response.ok) {
				alert('${product.name} added to cart!');
				handleClose();
			} else {
				alert("Failed to add item to cart.");
			}
		} catch (error) {
			console.error("Error adding item to cart:", error);
			alert("An error occurred. Please try again.");
		}
	};

	
	useEffect(() => {
		const background = document.querySelector('.background');
		const messageText = document.querySelector('.message');
		const scrollText = document.querySelector('.text-slide');
		const scrollCatalogue = document.querySelector('.catalogue');
		const searchCatalogue = document.querySelector('.search-bar');
		const searchAdvanced = document.querySelector('.search-advanced');

		// Captures the page scroll and causes effects accordingly.
		const handleScroll = () => {
			// Measures the current scroll position on the page.
			const scrollPosition = window.scrollY;
			const scrollTrigger = window.innerHeight * 0.5;

			//only update if it exists so that other pages dont get an error when visited
			if (background) {
				let newOpacity = 1 - scrollPosition / 500;
				background.style.opacity = Math.max(newOpacity, 0);
			}

			// Fades the initial greeting background image out while scrolling down.
			let newOpacity = 1 - scrollPosition / 500;
			if (newOpacity < 0) newOpacity = 0;
			background.style.opacity = newOpacity;

			// Slides in and shows the catalogue and searchbar
			if (scrollPosition > scrollTrigger) {
				messageText.classList.add('inactive');
				scrollText.classList.add('active');
				scrollCatalogue.classList.add('visible');
				searchCatalogue.classList.add('visible');
				searchAdvanced.classList.add('visible');
			}
			else {
				messageText.classList.remove('inactive');
				scrollText.classList.remove('active');
				scrollCatalogue.classList.remove('visible');
				searchCatalogue.classList.remove('visible');
				searchAdvanced.classList.remove('visible');
			}
		}
		window.addEventListener('scroll', handleScroll);
		window.addEventListener('load', handleScroll);
		return () => window.removeEventListener('scroll', handleScroll);
	});

	return (
		<div>
			<div className="background">
				<div className="message">
					<h1>4413-CLOTHING</h1>
					<h2>A functional website for all your clothing needs</h2>
					<h3>Owners: Timothy Tolstinev, Justin Oguntala, Eric Nguyen, Eiad Sayed Suliman</h3>
				</div>
			</div>

			{/*Everything catalogue-related begins here */}
			
			<div className="catalogue-wrapper">
				<div className="text-slide">Our Curated Catalogue</div>

				{/*Search functions via keywords*/}
				<input
					type='text'
					placeholder='Search Items...'
					value={searchTerm}
					onChange={(e) => setSearchTerm(e.target.value)}
					className='search-bar'
				/>

				{/*Advanced filters for sorting, categories, and brands*/}
				<div className="search-advanced">
					<div><label>Sort By:</label>
						<select value={sortOrder} onChange={(e) => setSortOrder(e.target.value)}>
							<option value="">Default</option>
							<option value="asc">A - Z</option>
							<option value="desc">Z - A</option>
							<option value="price-asc">Price: Low - High</option>
							<option value="price-desc">Price: High - Low</option>
						</select>
					</div>
					<div><label>Price Range:</label>
						<div className="price-range">
							<input type="number" placeholder="Min" value={minPrice ?? ""} onChange={(e) => setMinPrice(e.target.value ? Number(e.target.value) : null)} min="0" />
							<input type="number" placeholder="Max" value={maxPrice ?? ""} onChange={(e) => setMaxPrice(e.target.value ? Number(e.target.value) : null)} min="0" />
						</div>
					</div>
					<div><label>Category:</label>
						<select value={selectedCategory} onChange={(e) => setSelectedCategory(e.target.value)}>
							<option value="">Any</option>
							{category.map((category) => (
								<option key={category} value={category}>{category}</option>
							))}
						</select>
					</div>
					<div><label>Brand:</label>
						<select value={selectedBrand} onChange={(e) => setSelectedBrand(e.target.value)}>
							<option value="">Any</option>
							{brands.map((brand) => (
								<option key={brand} value={brand}>{brand}</option>
							))}
						</select>
					</div>
				</div>

				{/*Displays the products as clickable cards*/}
				<div className="catalogue">
					{filteredProduct.map((product, index) => (
						<div className='product-card' key={index} onClick={() => handleItemClick(product)}>
							<img src={product.image} class="image" />
							<info>
								<p className="product-name">{product.name}</p>
								<p className="product-price">${product.price}</p>
							</info>
						</div>
					))}
				</div>
			</div>

			{/*Displays more info about a product when clicked.
				Notably, the modal is separated by left side (product image) and right side (product info)*/}
			{selectedProduct && (
				<div className='product-modal' onClick={handleClose}>
					<div className='product-content' onClick={(e) => e.stopPropagation()}>
						<span className='close-button' onClick={handleClose}>&times;</span>
						<div className="product-modal-inner">
							<div className="product-modal-left">
								<img src={selectedProduct.image} className="product-image-full" />
							</div>
							<div className="product-modal-right">
								<div className="product-modal-content">
									<p className="product-name">{selectedProduct.name}</p>
									<p className="product-price">${selectedProduct.price}</p>
									<p className="product-stock">Only <span className="product-stock-amount">{selectedProduct.stock}</span> in stock!</p>
									<p className="product-description">{selectedProduct.description}</p>
								</div>
								<button className='add-to-cart' onClick={() => handleAddToCart(selectedProduct)}>Add to cart</button>
							</div>
						</div>
					</div>
				</div>
			)}
			<div className="footer">
				<p>&copy; {new Date().getFullYear()} 4413-Clothing. All rights reserved. Created for EECS4413M.</p>
				<p>Timothy Tolstinev, Justin Oguntala, Eric Nguyen, Eiad Sayed Suliman</p>
			</div>
		</div>
	);
}

export default HomePage;
