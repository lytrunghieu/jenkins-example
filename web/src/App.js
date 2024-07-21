// src/App.js
import React from 'react';

function App() {
  return (
    <div>
      <h1 id="welcome-message">Welcome to My React App</h1>
      <button id="get-started-button" onClick={() => alert('Button Clicked!')}>
        Get Started
      </button>
    </div>
  );
}

export default App;
