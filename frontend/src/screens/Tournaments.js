import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import ErrorMessage from '../components/ErrorMessage';
import { fetchUserData } from '../utils/userUtils';
import { fetchEligibleTournaments, fetchAllTournaments, fetchHistory, handleDeleteTournament, handleRegister, handleWithdraw } from '../utils/tournamentUtils';

const TournamentManagement = () => {
    const [errorMessage, setErrorMessage] = useState('');
    const [tournaments, setTournaments] = useState([]);
    const [history, setHistory] = useState([]);
    const [user, setUser] = useState({});

    // When page loaded, fetch user data
    useEffect(() => {
        fetchUserData(setErrorMessage, setUser);
    }, []);

    useEffect(() => {
        if (user.userRole === "ROLE_USER") {
            fetchEligibleTournaments(user.elo, setErrorMessage, setTournaments);
            fetchHistory(user.username, setErrorMessage, setHistory);
        } else {
            fetchAllTournaments(setErrorMessage, setTournaments);
        }
    }, [user])

    const formatDate = (dateString) => {
        const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
        return new Date(dateString).toLocaleDateString('en-GB', options); // Format to dd/mm/yyyy
    };

    const handleSubmit = async (e, tournamentId, action) => {
        e.preventDefault(); // Stop default form submission
        const userData = {
            username: user.username,
            elo: user.elo
        };
        switch (action) {
            case 'register':
                handleRegister(tournaments, tournamentId, userData, setErrorMessage, setTournaments);
                break;
            case 'withdraw':
                handleWithdraw(tournaments, tournamentId, userData, setErrorMessage, setTournaments);
                break;
            case 'delete':
                handleDeleteTournament(tournaments, tournamentId, setErrorMessage, setTournaments);
                break;
            default:
                setErrorMessage("Unknown Action.");
        }
    };

    return (
        <>
            <Navbar userRole={user.userRole} />
            <main>
                <h1>Tournament Management</h1>
                <ErrorMessage message={errorMessage} />

                <div className="view">
                    {user.userRole === 'ROLE_ADMIN' && (
                        <p>
                            Add New Tournament:
                            <Link to="/addTournament" className="admin-link">
                                <button type='button'>Add Tournament</button>
                            </Link>
                        </p>
                    )}
                    <h3>Available Tournaments.</h3>
                    {tournaments.length === 0 ? (
                        <p>No available tournaments at the moment.</p> // Message for empty tournaments
                    ) : (
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
                                {tournaments.map((tournament) => {
                                    const hasTournamentStarted = new Date() > new Date(tournament.date);
                                    const overloaded = tournament.size - tournament.userTournaments.length;
                                    const vacancies = overloaded < 0 ? 0 : overloaded;

                                    return (
                                        <tr key={tournament.id}>
                                            <td>{tournament.title}</td>
                                            <td>{tournament.minElo}</td>
                                            <td>{tournament.maxElo}</td>
                                            <td>{formatDate(tournament.date)}</td>
                                            <td>{vacancies}</td>
                                            <td>
                                                {user.userRole === 'ROLE_USER' && (
                                                    <>
                                                        <Link to={`/tournaments/${tournament.id}/rounds/1`}>
                                                            <button>View Details</button>
                                                        </Link>
                                                        <form onSubmit={(e) => handleSubmit(e, tournament.id, "register")}>
                                                            <button type="submit" disabled={hasTournamentStarted}>Register</button>
                                                        </form>
                                                        <form onSubmit={(e) => handleSubmit(e, tournament.id, "withdraw")}>
                                                            <button type="submit">Withdraw</button>
                                                        </form>
                                                    </>
                                                )}
                                                {user.userRole === 'ROLE_ADMIN' && (
                                                    <div className="admin-buttons">
                                                        <Link to={`/tournaments/${tournament.id}/rounds/1`} className="admin-link">
                                                            <button type='button'>View Match Details</button>
                                                        </Link>
                                                        <Link to={`/updateTournament/${tournament.id}`} className="admin-link">
                                                            <button type="button">Update Tournament</button>
                                                        </Link>
                                                        <form onSubmit={(e) => handleSubmit(e, tournament.id, "delete")}>
                                                            <button type="submit">Delete Tournament</button>
                                                        </form>
                                                    </div>
                                                )}
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>)}
                    {user.userRole === 'ROLE_USER' && history.length > 0 && ( // Check if user is ROLE_USER and history is not empty
                        <>
                            <h3>Past Tournaments</h3>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Title</th>
                                        <th>View</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {history.map((tournament) => (
                                        <tr key={tournament.id}>
                                            <td>{tournament.title}</td>
                                            <td>
                                                <Link to={`/tournaments/${tournament.id}/rounds/1`}>
                                                    <button>View</button>
                                                </Link>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </>
                    )}
                    {user.userRole === 'ROLE_USER' && history.length === 0 && ( // Check if user is ROLE_USER and history is empty
                        <p>No past tournaments available.</p> // Message for empty history
                    )}
                </div>
            </main>
        </>
    );
};

export default TournamentManagement;
