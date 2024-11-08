import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import NavLink from './NavLink';

const Navbar = ({ userRole }) => {
    const navigate = useNavigate();

    const handleLogout = (e) => {
        e.preventDefault(); // Prevent default form submission
        navigate("/login"); // Navigate to the login page
        localStorage.removeItem('jwtToken'); // Remove jwtToken from localStorage
    };

    const renderNavItems = () => (
        <>
            <NavLink to="/tournaments" label="Tournaments" />
            {userRole === 'ROLE_USER' && <NavLink to="/profile" label="Profile" />}
        </>
    );

    return (
        <nav className="navbar">
            <div className="nav-container">
                <Link className="navbar-brand" to="/tournaments">
                    Chess Tournament Manager
                </Link>

                <ul className="navbar-nav">
                    {renderNavItems()}
                </ul>

                <LogoutButton onLogout={handleLogout} />
            </div>
        </nav>
    );
};

const LogoutButton = ({ onLogout }) => {
    return (
        <form onSubmit={onLogout} className="logout-form">
            <button type="submit" className="logout-button">Log Out</button>
        </form>
    );
};


export default Navbar;