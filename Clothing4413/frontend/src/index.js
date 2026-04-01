import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

import { AuthProvider } from "./Context/AuthContext";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <AuthProvider>
      <App />
    </AuthProvider>
  </React.StrictMode>
);

document.addEventListener('DOMContentLoaded', () => {
  window.addEventListener('scroll', function () {
    const scrollPosition = window.scrollY;

    // Only run if background element exists (homepage only)
    const background = document.querySelector('.background');
    if (background) {
      let newOpacity = 1 - scrollPosition / 500;
      if (newOpacity < 0) newOpacity = 0;
      background.style.opacity = newOpacity;
    }

    const scrollText = document.querySelector('.text-slide');
    const scrollCatalogue = document.querySelector('.catalogue');
    if (scrollText && scrollCatalogue) {
      if (scrollPosition > 400) {
        scrollText.classList.add('active');
        scrollCatalogue.classList.add('visible');
      } else {
        scrollText.classList.remove('active');
        scrollCatalogue.classList.remove('visible');
      }
    }
  });
});
// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
