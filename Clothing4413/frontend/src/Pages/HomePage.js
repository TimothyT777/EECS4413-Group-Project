import '../Styles/HomePage.css';

function HomePage(){
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
		      <div className="catalogue">
		        <div className="item-card">
		          <img src="/img/shirt1.png" class="image"/>
		          <info>
		            <p className="item-name">Shirt 1</p>
		            <p className="item-price">$19.99</p>
		          </info>
		        </div>
		        <div className="item-card">
		          <img src="/img/shirt2.png" class="image"/>
		          <info>
		            <p className="item-name">Shirt 2</p>
		            <p className="item-price">$23.99</p>
		          </info>
		        </div>
		        <div className="item-card">
		          <img src="/img/shirt3.png" class="image"/>
		          <info>
		            <p className="item-name">Shirt 3</p>
		            <p className="item-price">$27.99</p>
		          </info>
		        </div>
		        <div className="item-card">
		          <img src="/img/shirt4.png" class="image"/>
		          <info>
		            <p className="item-name">Shirt 4</p>
		            <p className="item-price">$15.99</p>
		          </info>
		        </div>
		      </div>
		    </div>
			</div>
	);
}

export default HomePage;