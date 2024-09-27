import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import Navbar from '../components/NavBar';

const TournamentManagement = ({ tournaments, userRole: propUserRole, message, errorMessage }) => {
    const location = useLocation(); // passed via redirect
    const { role : stateUserRole} = location.state; // Default to undefined if state is not passed
    // if called as function >> passed as redirect
    const userRole = propUserRole || stateUserRole;

    return (
        <><Navbar userRole={userRole}/>
        <main>
            {message && <div><h3 style={{ color: 'green' }}>{message}</h3></div>}
            {errorMessage && <div><h3 style={{ color: 'red' }}>{errorMessage}</h3></div>}
            <h1>Tournament Management</h1>

            {userRole === 'ROLE_USER' && (
                <div className="user-view">
                    <p>Available Tournaments. Registration Period: Until Date of Tournament</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Tournament</th>
                                <th>Min Elo</th>
                                <th>Max Elo</th>
                                <th>Date</th>
                                <th>Vacancies</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {tournaments.map((tournament) => (
                                <tr key={tournament.id}>
                                    <td>{tournament.title}</td>
                                    <td>{tournament.minElo}</td>
                                    <td>{tournament.maxElo}</td>
                                    <td>{new Date(tournament.date).toLocaleDateString()}</td>
                                    <td>{tournament.size - tournament.participants.length}</td>
                                    <td>
                                        <Link to={`/tournaments/${tournament.id}`}>
                                            <button>View Details</button>
                                        </Link>
                                        <form action={`/register/${tournament.id}`} method="post">
                                            <button type="submit">Register</button>
                                        </form>
                                        <form action={`/withdraw/${tournament.id}`} method="post">
                                            <button type="submit">Withdraw</button>
                                        </form>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}

            {userRole === 'ROLE_ADMIN' && (
                <div className="admin-view">
                    <p>Manage Tournament Here.</p>
                    <form action="/tournaments" method="post">
                        <button type="submit">Add New Tournament</button>
                    </form>
                    <table>
                        <thead>
                            <tr>
                                <th>Tournament</th>
                                <th>Min Elo</th>
                                <th>Max Elo</th>
                                <th>Date</th>
                                <th>Vacancies</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {tournaments.map((tournament) => (
                                <tr key={tournament.id}>
                                    <td>{tournament.title}</td>
                                    <td>{tournament.minElo}</td>
                                    <td>{tournament.maxElo}</td>
                                    <td>{new Date(tournament.date).toLocaleDateString()}</td>
                                    <td>{tournament.size - tournament.participants.length}</td>
                                    <td>
                                        <Link to={`/tournaments/${tournament.id}`}>
                                            <button>Update Match Details</button>
                                        </Link>
                                        <Link to={`/tournaments/${tournament.id}`}>
                                            <button>Update Tournament</button>
                                        </Link>
                                        <form action={`/tournaments/${tournament.id}`} method="post">
                                            <button type="submit">Delete Tournament</button>
                                        </form>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </main></>
    );
};

export default TournamentManagement;
