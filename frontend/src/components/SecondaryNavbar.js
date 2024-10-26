import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

const SecondaryNavbar = ({ tournament }) => {
    const navigate = useNavigate();

    const handleBack = (e) => {
        e.preventDefault();
        navigate('/tournaments'); // Navigate back to tournaments
    };

    return (
        <nav className="secondary-navbar">
            <div className="nav-container">
                <h1 className="navbar-brand">{tournament.title}</h1>
                <ul className="navbar-nav">
                    {renderNavLinks(tournament.id)}
                </ul>
                <button onClick={handleBack} className="back-button">Back</button>
            </div>
        </nav>
    );
};

const renderNavLinks = (tournamentId) => (
    <>
        <li className="nav-item">
            <Link className="nav-link" to={`/tournaments/${tournamentId}/rounds/1`}>Rounds</Link>
        </li>
        <li className="nav-item">
            <Link className="nav-link" to={`/tournaments/${tournamentId}/standings`}>Standings</Link>
        </li>
    </>
);

export default SecondaryNavbar;
