import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from './Pages/HomePage';
import Navigation from './Pages/Navigation';
import LoginPage from './Pages/LoginPage';
import CartPage from './Pages/CartPage';

import React, { useState, useEffect } from 'react';

function App() {
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetch('api/test').then(response => response.text()).then(data => setMessage(data));
  }, []);

  return (
	<Router>
	<Navigation />
      <Routes>
        <Route path="/" element={<HomePage />} />
       <Route path="/login" element={<LoginPage />} />
        <Route path="/cart" element={<CartPage />} />
      </Routes>
    </Router>
  )
}

export default App;
