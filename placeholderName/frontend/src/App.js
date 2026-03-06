import logo from './logo.svg';
import './App.css';

import React, { useState, useEffect } from 'react';

function App() {
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetch('api/test').then(response => response.text()).then(data => setMessage(data));
  }, []);

  return (
    <div className="App">
    </div>
  )
}

export default App;
