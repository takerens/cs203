// src/components/Navbar.js
import React from 'react';
import { Link } from 'react-router-dom';
import '../index.css'; // Import a CSS file for styling

const Navbar = () => {
    const handleLogout = (e) => {
        e.preventDefault();
        alert("You have logged out");
        // Add your logout logic here (e.g., clearing user data, redirecting, etc.)
    };

    return (
        <nav className="navbar">
            <div className="nav-container">
                <Link className="navbar-brand" to="/">Chess Tournament Manager</Link>
                <ul className="navbar-nav">
                    <li className="nav-item"><Link className="nav-link" to="/tournaments">Tournaments</Link></li>
                    <li className="nav-item"><Link className="nav-link" to="/profile">Profile</Link></li>
                </ul>
                <form onSubmit={handleLogout} className="logout-form">
                    <button type="submit" className="logout-button">Log Out</button>
                </form>
            </div>
        </nav>
    );
};

export default Navbar;
