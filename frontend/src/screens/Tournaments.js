import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import ErrorMessage from '../components/ErrorMessage';

const TournamentManagement = () => {
    const [errorMessage, setErrorMessage] = useState('');
    const [tournaments, setTournaments] = useState([]);
    const [history, setHistory] = useState([]);
    const [user, setUser] = useState({});
    const [loading, setLoading] = useState(true); // Add loading state
    const [standings, setStandings] = useState({});
    const navigate = useNavigate();

    // When page loaded, fetch user data
    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await fetch("http://localhost:8080/user", { method: 'GET' });

                if (!response.ok) {
                    const errorResponse = await response.json(); // Get error message from response
                    console.error('Fetching User:', errorResponse); // Log error for debugging
                    throw new Error(errorResponse.message); // General error message
                }

                const userData = await response.json();
                console.log("User Data: " + JSON.stringify(userData)); // View Data for Debugging
                setUser(userData);
            } catch (error) {
                setErrorMessage(error.message);
            } finally {
                setLoading(false);
            }
        };
        fetchUserData();
    }, []);

    useEffect(() => {
        if (user.userRole === "ROLE_USER") {
            fetchEligibleTournamentData();
            fetchHistory();
        } else {
            fetchAllTournamentData();
        }
    }, [user])

    const fetchStandings = async (tournamentId) => {
        try {
            const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}/standings`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
            });

            if (!response.ok) {
                const errorResponse = await response.json();
                console.error('Fetching Tournament Data:', errorResponse);
                throw new Error(errorResponse.message);
            }

            const standingsData = await response.json();
            setStandings(prevStandings => ({
                ...prevStandings,
                [tournamentId]: standingsData
            }));
        } catch (error) {
            setErrorMessage("Fetch Standings Data Error: " + error.message);
        }
    };

    const fetchAllTournamentData = async () => {
        try {
            const response = await fetch("http://localhost:8080/tournaments", {
                method: 'GET',
                // headers: { 'Content-Type': 'application/json' },
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Fetching Tournament Data:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            const tournamentData = await response.json();
            console.log("Tournament All Data: " + JSON.stringify(tournamentData, null, 2)); // View Data for Debugging
            setTournaments(tournamentData);

            // Fetch standings for each tournament
            tournamentData.forEach(tournament => {
                fetchStandings(tournament.id);
            });
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const fetchEligibleTournamentData = async () => {
        try {
            const response = await fetch(`http://localhost:8080/tournaments/byElo/${user.elo}`, {
                method: 'GET',
                // headers: { 'Content-Type': 'application/json' },
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Fetching Tournament Data:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            const tournamentData = await response.json();
            console.log("Tournament Eligible Data: " + JSON.stringify(tournamentData, null, 2)); // View Data for Debugging
            setTournaments(tournamentData);

            // Fetch standings for each tournament
            tournamentData.forEach(tournament => {
                fetchStandings(tournament.id);
            });
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const fetchHistory = async () => {
        try {
            const response = await fetch(`http://localhost:8080/tournaments/byUser/${user.username}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Fetching Tournament Data:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            const tournamentData = await response.json();
            console.log("Tournament User Data: " + JSON.stringify(tournamentData, null, 2)); // View Data for Debugging
            setHistory(tournamentData);

        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const formatDate = (dateString) => {
        const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
        return new Date(dateString).toLocaleDateString('en-GB', options); // Format to dd/mm/yyyy
    };

    const getTournamentTitleById = (id) => {
        const tournament = tournaments.find(t => t.id === id);
        return tournament ? tournament.title : null; // Return title or null if not found
    };

    const handleRegister = async (e, tournamentId) => {
        e.preventDefault(); // Stop default form submission
        setErrorMessage(''); // Clear previous error message

        try {
            const userData = {
                "username": user.username
            }

            const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Trying to Register:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            // User registered successfully (200)
            alert(`${user.username} has registered for ${getTournamentTitleById(tournamentId)}.`); // Success message
            fetchEligibleTournamentData(); // Refresh Page
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const handleWithdraw = async (e, tournamentId) => {
        e.preventDefault(); // Stop default form submission
        setErrorMessage(''); // Clear previous error message

        try {
            const userData = {
                "username": user.username
            }
            const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}/withdraw`, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Trying to Withdraw:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            // User withdrawn successfully (200)
            alert(`${user.username} has withdrawn from ${getTournamentTitleById(tournamentId)}.`); // Success message
            fetchEligibleTournamentData(); // Refresh Page
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const handleDelete = async (e, tournamentId) => {
        e.preventDefault(); // Stop default form submission
        setErrorMessage(''); // Clear previous error message

        try {
            const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}`, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Trying to Delete:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            // User withdrawn successfully (200)
            alert(`${getTournamentTitleById(tournamentId)} has been deleted.`); // Success message
            fetchAllTournamentData(); // Refresh Page
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const handleAdd = async (e) => {
        e.preventDefault();
        navigate('/addTournament');
    };

    if (loading) {
        return <div>Loading...</div>; // Display a loading message while user data is being fetched
    }

    return (
        <>
            <Navbar userRole={user.userRole} />
            <main>
                <ErrorMessage message={errorMessage} />
                <h1>Tournament Management</h1>

                <div className="view">
                    {user.userRole === 'ROLE_ADMIN' && (
                        <p>
                            Add New Tournament:
                            <form onSubmit={handleAdd}>
                                <button type="submit">Add Tournament</button>
                            </form>
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
                                    const tournamentStandings = standings[tournament.id] || [];
                                    const vacancies = tournament.size - tournamentStandings.length - 1;

                                    return (
                                        <tr key={tournament.id}>
                                            <td>{tournament.title}</td>
                                            <td>{tournament.minElo}</td>
                                            <td>{tournament.maxElo}</td>
                                            <td>{formatDate(tournament.date)}</td>
                                            <td>{vacancies}</td> {/* - tournament.participants.length*/}
                                            <td>
                                                {user.userRole === 'ROLE_USER' && (
                                                    <>
                                                        <Link to={`/tournaments/${tournament.id}/rounds/1`}>
                                                            <button>View Details</button>
                                                        </Link>
                                                        <form onSubmit={(e) => handleRegister(e, tournament.id)}>
                                                            <button type="submit" disabled={hasTournamentStarted}>Register</button>
                                                        </form>
                                                        <form onSubmit={(e) => handleWithdraw(e, tournament.id)}>
                                                            <button type="submit">Withdraw</button>
                                                        </form>
                                                    </>
                                                )}
                                                {user.userRole === 'ROLE_ADMIN' && (
                                                    <div className="admin-buttons">
                                                        <Link to={`/tournaments/${tournament.id}/rounds/1`} className="admin-link">
                                                            <button>View Match Details</button>
                                                        </Link>
                                                        <Link to={`/updateTournament/${tournament.id}`} className="admin-link">
                                                            <button type="button">Update Tournament</button>
                                                        </Link>
                                                        <form onSubmit={(e) => handleDelete(e, tournament.id)}>
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
