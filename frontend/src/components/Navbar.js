// src/components/Navbar.js
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

const Navbar = ({ userRole }) => {
    const navigate = useNavigate();
    
    const handleLogout = (e) => {
        e.preventDefault();
        alert("You have logged out");
        navigate("/login");
        // Add your logout logic here (e.g., clearing user data, redirecting, etc.)
    };

    return (
        <nav className="navbar">
            <div className="nav-container">
                <Link className="navbar-brand" to="/tournaments">
                    Chess Tournament Manager
                </Link>
                
                
                <ul className="navbar-nav">
                    <li className="nav-item"><Link className="nav-link" to="/tournaments">Tournaments</Link></li>
                    {userRole === 'ROLE_USER' && (
                        <li className="nav-item"><Link className="nav-link" to="/profile">Profile</Link></li>
                    )}
                </ul>
                
                
                <form onSubmit={handleLogout} method="post" className="logout-form">
                    <button type="submit" className="logout-button">Log Out</button>
                </form>
            </div>
        </nav>
    );
};

export default Navbar;