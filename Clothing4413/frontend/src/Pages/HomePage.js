import '../Styles/HomePage.css';
import { useState, useEffect } from "react";
import '../index.css';

function HomePage(){
	const [searchTerm, setSearchTerm] = useState("");
	const [selectedProduct, setSelectedProduct] = useState(null);
	const [selectedBrand, setSelectedBrand] = useState("");
	const [selectedCategory, setSelectedCategory] = useState("");
	const [sortOrder, setSortOrder] = useState("");
	const [minPrice, setMinPrice] = useState("");
	const [maxPrice, setMaxPrice] = useState("");


	// Used for searching up items and pulling info from the database.
	const products = [
			{ name: "Shirt 1", price: 19.99, stock: 5, image: "/img/shirt1.png", category: "Shirt", brand: "Gucci"	, description: "Description for shirt ONE here" },
			{ name: "Shirt 2", price: 23.99, stock: 3, image: "/img/shirt2.png", category: "Shirt", brand: "Gucci"	, description: "Description for shirt TWO here" },
			{ name: "Shirt 3", price: 27.99, stock: 1, image: "/img/shirt3.png", category: "Shirt", brand: "Nike"	, description: "Description for shirt THREE here" },
			{ name: "Shirt 4", price: 15.99, stock: 99, image: "/img/shirt4.png", category: "Pants", brand: "Your Mom", description: "The FitnessGram™ Pacer Test is a multistage aerobic capacity test that progressively gets more difficult as it continues. The 20 meter pacer test will begin in 30 seconds. Line up at the start. The running speed starts slowly, but gets faster each minute after you hear this signal. [beep] A single lap should be completed each time you hear this sound. [ding] Remember to run in a straight line, and run as long as possible. The second time you fail to complete a lap before the sound, your test is over. The test will begin on the word start. On your mark, get ready, start." },
		];

	// Advanced filters combing search term, category, brand, and price range.
	const brands = [...new Set(products.map(product => product.brand))];
	const category = [...new Set(products.map(product => product.category))];
	const filteredProduct = products.filter((products) => {
		const matchedTerm = products.name.toLowerCase().includes(searchTerm.toLowerCase())
		const matchedCategory = selectedCategory ? products.category === selectedCategory : true;
		const matchedBrand = selectedBrand ? products.brand === selectedBrand : true;
		const matchedMinPrice = minPrice !== "" ? products.price >= parseFloat(minPrice) : true;
		const matchedMaxPrice = maxPrice !== "" ? products.price <= parseFloat(maxPrice) : true;
		return matchedTerm && matchedCategory && matchedBrand && matchedMinPrice && matchedMaxPrice;
	});
	
	// Sort by alphabetical or by price.
	if (sortOrder === "asc") {
		filteredProduct.sort((a, b) => a.name.localeCompare(b.name));
	} else if (sortOrder === "desc") {
		filteredProduct.sort((a, b) => b.name.localeCompare(a.name));
	} else if (sortOrder === "price-asc") {
		filteredProduct.sort((a, b) => a.price - b.price);
	} else if (sortOrder === "price-desc") {
		filteredProduct.sort((a, b) => b.price - a.price);
	}
	

	// Handles clicking on catalogue items.
	const handleItemClick = (products) => setSelectedProduct(products);
	const handleClose = () => setSelectedProduct(null);

	useEffect(() => {
		// Captures the page scroll and causes effects accordingly.
		const handleScroll = () => {
			// Measures the current scroll position on the page.
			const scrollPosition = window.scrollY;

			// Fades the initial greeting background image out while scrolling down.
			const background = document.querySelector('.background');
			let newOpacity = 1 - scrollPosition / 500;
			if (newOpacity < 0) newOpacity = 0;
			background.style.opacity = newOpacity;

			// Slides in and shows the catalogue and searchbar
			const scrollText = document.querySelector('.text-slide');
			const scrollCatalogue = document.querySelector('.catalogue');
			const searchCatalogue = document.querySelector('.search-bar');
			const searchAdvanced = document.querySelector('.search-advanced');
			if (scrollPosition > 400) {
				scrollText.classList.add('active');
				scrollCatalogue.classList.add('visible');
				searchCatalogue.classList.add('visible');
				searchAdvanced.classList.add('visible');
			}
			else {
				scrollText.classList.remove('active');
				scrollCatalogue.classList.remove('visible');
				searchCatalogue.classList.remove('visible');
				searchAdvanced.classList.remove('visible');
			}
		}
		window.addEventListener('scroll', handleScroll);
		return () => window.removeEventListener('scroll', handleScroll);
	});

	
	
	

	return(
		<div>
			<div className="background"></div>
			<div className="message">
				<h1>WELCOME TO A REAL WEBSITE</h1>
				<h2>This is not a scam web-site</h2>
				<h3>You can tell by the way it is</h3>
			</div>

			{/*Everything catalogue-related begins here */}
			<div className="text-slide">Our (Legitimate) Catalogue</div>
			<div className="catalogue-wrapper">

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
							<input type="number" placeholder="Min" value={minPrice} onChange={(e) => setMinPrice(e.target.value)} min="0"/>
							<input type="number" placeholder="Max" value={maxPrice} onChange={(e) => setMaxPrice(e.target.value)} min="0"/>
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
					{filteredProduct.map((products, index) => (
						<div className='product-card' key={index} onClick={() => handleItemClick(products)}>
						<img src={products.image} class="image"/>
						<info>
							<p className="product-name">{products.name}</p>
							<p className="product-price">${products.price}</p>
						</info>
						</div>
					))}

					{/*Displays more info about a product when clicked.
					   Notably, the modal is separated by left side (product image) and right side (product info)*/}
					{selectedProduct && (
						<div className='product-modal' onClick={handleClose}>
							<div className='product-content' onClick={(e) => e.stopPropagation()}>
								<span className='close-button' onClick={handleClose}>&times;</span>
								<div className="product-modal-inner">
									<div className="product-modal-left">
										<img src={selectedProduct.image} className="product-image-full"/>
									</div>
									<div className="product-modal-right">
										<div className="product-modal-content">
											<p className="product-name">{selectedProduct.name}</p>
											<p className="product-price">${selectedProduct.price}</p>
											<p className="product-stock">Only <span className="product-stock-amount">{selectedProduct.stock}</span> in stock!</p>
											<p className="product-description">{selectedProduct.description}</p>
										</div>
										<button className='add-to-cart'>Add to cart</button>
									</div>
								</div>
							</div>
						</div>
					)}
				</div>
			</div>
		</div>
	);
}

export default HomePage;
