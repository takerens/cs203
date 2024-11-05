import React from 'react';
import { useNavigate } from 'react-router-dom';
import NavLink from './NavLink';

const SecondaryNavbar = ({ tournament }) => {
    const navigate = useNavigate();

    return (
        <nav className="secondary-navbar">
            <div className="nav-container">
                <h1 className="navbar-brand">{tournament.title}</h1>
                <ul className="navbar-nav">
                    <NavLink to={`/tournaments/${tournament.id}/rounds/1`} label="Rounds" />
                    <NavLink to={`/tournaments/${tournament.id}/standings`} label="Standings" />
                </ul>
                <button onClick={() => navigate('/tournaments')} className="back-button">Back</button>
            </div>
        </nav>
    );
};

export default SecondaryNavbar;
