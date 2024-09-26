// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Registration from './screens/Registration';
import Login from './screens/Login'; 
import Navbar from './components/Navbar';

const App = () => {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} /> 
        <Route path="/signup" element={<Registration />} />
        <Route path="/login" element={<Login />} />
        {/* Add more routes here as needed */}
      </Routes>
    </Router>
  );
};

export default App;
