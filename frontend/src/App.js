// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Registration from './screens/Registration';
import Login from './screens/Login'; 
import TournamentManagement from './screens/Tournaments';

const App = () => {
  return (
    <Router>
      {/* <Navbar userRole={"ROLE_ADMIN"}/> */}
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} /> 
        <Route path="/signup" element={<Registration />} />
        <Route path="/login" element={<Login />} />
        <Route path="/tournaments" element={<TournamentManagement />} />
        {/* Add more routes here as needed */}
      </Routes>
    </Router>
  );
};

export default App;
