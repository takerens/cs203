import React from 'react';
import { Link } from 'react-router-dom';

const NavLink = ({ to, label }) => (
    <li className="nav-item">
        <Link className="nav-link" to={to}>{label}</Link>
    </li>
);

export default NavLink;