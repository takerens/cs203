// src/components/SecondaryNavbar.js
import React from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';

const SecondaryNavbar = ({ tournament }) => {
    const navigate = useNavigate(); // For programmatic navigation

    const handleBack = (e) => {
        e.preventDefault();
        navigate('/tournaments'); // Navigate back to tournaments
    };

    return (
        <nav className="secondary-navbar">
            <div className="nav-container">
                <h1 className="navbar-brand">{tournament.title}</h1>
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <Link className="nav-link" to={`/tournaments/${tournament.id}`}>Players</Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to={`/tournaments/${tournament.id}/rounds/1`}>Rounds</Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to={`/tournaments/${tournament.id}/standings/1`}>Standings</Link>
                    </li>
                </ul>
                <button onClick={handleBack}>Back</button>
            </div>
        </nav>
    );
};

export default SecondaryNavbar;
