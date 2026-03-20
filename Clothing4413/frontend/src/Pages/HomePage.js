import '../Styles/HomePage.css';
import { useState, useEffect } from "react";
import '../index.css';

function HomePage(){
	const [searchTerm, setSearchTerm] = useState("");
	const [selectedItem, setSelectedItem] = useState(null);
	const [products, setProducts] = useState([]);

	//used to get products from database
	useEffect(() => {
    fetch("http://localhost:8080/products")
      .then(res => res.json())
      .then(data => setProducts(data))
      .catch(err => console.error(err));
  }, []);

	// Used for searching up items and pulling info from the database.
	// const items = [
	// 		{ name: "Shirt 1", price: 19.99, image: "/img/shirt1.png", description: "Description for shirt ONE here" },
	// 		{ name: "Shirt 2", price: 23.99, image: "/img/shirt2.png", description: "Description for shirt TWO here" },
	// 		{ name: "Shirt 3", price: 27.99, image: "/img/shirt3.png", description: "Description for shirt THREE here" },
	// 		{ name: "Shirt 4", price: 15.99, image: "/img/shirt4.png", description: "The FitnessGram™ Pacer Test is a multistage aerobic capacity test that progressively gets more difficult as it continues. The 20 meter pacer test will begin in 30 seconds. Line up at the start. The running speed starts slowly, but gets faster each minute after you hear this signal. [beep] A single lap should be completed each time you hear this sound. [ding] Remember to run in a straight line, and run as long as possible. The second time you fail to complete a lap before the sound, your test is over. The test will begin on the word start. On your mark, get ready, start." },
	// 	];
	const searchedItems = products.filter(
		(products) => products.name.toLowerCase().includes(searchTerm.toLowerCase())
	);

	// Handles clicking on catalogue items.
	const handleItemClick = (products) => setSelectedItem(products);
	const handleClose = () => setSelectedItem(null);

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
			if (scrollPosition > 400) {
				scrollText.classList.add('active');
				scrollCatalogue.classList.add('visible');
				searchCatalogue.classList.add('visible');
			}
			else {
				scrollText.classList.remove('active');
				scrollCatalogue.classList.remove('visible');
				searchCatalogue.classList.remove('visible');
			}
		}
		window.addEventListener('scroll', handleScroll);
		return () => window.removeEventListener('scroll', handleScroll);
	}, []);

	
	
	

	return(
		<div>
			<div className="background"></div>
			<div className="message">
				<h1>WELCOME TO A REAL WEBSITE</h1>
				<h2>This is not a scam web-site</h2>
				<h3>You can tell by the way it is</h3>
			</div>

			<div className="text-slide">Our (Legitimate) Catalogue</div>
			<div className="catalogue-wrapper">
				<input 
					type='text' 
					placeholder='Search Items...' 
					value={searchTerm}
					onChange={(e) => setSearchTerm(e.target.value)}
					className='search-bar'
				/>
				<div className="catalogue">
					{searchedItems.map((products, index) => (
						<div className='item-card' key={index} onClick={() => handleItemClick(products)}>
						<img src={products.image} class="image"/>
						<info>
							<p className="item-name">{products.name}</p>
							<p className="item-price">${products.price}</p>
						</info>
						</div>
					))}
					{selectedItem && (
						<div className='item-modal' onClick={handleClose}>
							<div className='item-content' onClick={(e) => e.stopPropagation()}>
								<span className='close-button' onClick={handleClose}>&times;</span>
								<div className="item-modal-inner">
									<div className="item-modal-left">
										<img src={selectedItem.image} className="item-image-full"/>
									</div>
									<div className="item-modal-right">
										<div className="item-modal-content">
											<p className="item-name">{selectedItem.name}</p>
											<p className="item-price">${selectedItem.price}</p>
											<p className="item-description">{selectedItem.description}</p>
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
