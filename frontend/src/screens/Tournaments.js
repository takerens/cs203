import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import ErrorMessage from '../components/ErrorMessage';

const TournamentManagement = () => {
    const [errorMessage, setErrorMessage] = useState('');
    const [tournaments, setTournaments] = useState([]);
    const [user, setUser] = useState({});
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
                console.log("User Data: " + userData); // View Data for Debugging
                const { username, password, userRole } = userData; // Destructure to get the needed properties
                setUser({ username, password, userRole }); // Set user state
            } catch (error) {
                setErrorMessage(error.message);
            }
        };

        fetchUserData();
        fetchTournamentData();
    }, []);

    const fetchTournamentData = async () => {
        try {
            const response = await fetch("http://localhost:8080/tournaments", {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Fetching Tournament Data:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            const tournamentData = await response.json();
            console.log("Tournament Data: " + JSON.stringify(tournamentData, null, 2)); // View Data for Debugging
            setTournaments(tournamentData);

        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const formatDate = (dateString) => {
        const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
        return new Date(dateString).toLocaleDateString('en-GB', options); // Format to dd/mm/yyyy
    };

    const handleRegister = async (e, tournamentId) => {
        e.preventDefault(); // Stop default form submission
        setErrorMessage(''); // Clear previous error message

        try {
            const response = await fetch(`http://localhost:8080/tournament/${tournamentId}/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(user),
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Trying to Register:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            // User registered successfully (200)
            const responseData = await response.json(); // Tournament
            alert(`${user.username} has registered for ${responseData.title}.`); // Success message
            fetchTournamentData(); // Refresh Page
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const handleWithdraw = async (e, tournamentId) => {
        e.preventDefault(); // Stop default form submission
        setErrorMessage(''); // Clear previous error message

        try {
            const response = await fetch(`http://localhost:8080/tournament/${tournamentId}/withdraw`, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(user),
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Trying to Withdraw:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            // User withdrawn successfully (200)
            const responseData = await response.json(); // Tournament
            alert(`${user.username} has withdrawn from ${responseData.title}.`); // Success message
            fetchTournamentData(); // Refresh Page

        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    // ToDo: Requests for these reference above
    const handleDelete = async (e, tournamentId) => {
        // Delete to (check tournament controller)
    };

    const handleAdd = async (e) => {
        e.preventDefault();
        navigate('/addTournament');
    };

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
                    <p>Available Tournaments.</p>
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
                            {/* {tournaments && tournaments.length > 0 ? ( */}
                                {tournaments.map((tournament) => {
                                const hasTournamentStarted = new Date() > new Date(tournament.date);

                                return (
                                <tr key={tournament.id}> 
                                    <td>{tournament.title}</td>
                                    <td>{tournament.minElo}</td>
                                    <td>{tournament.maxElo}</td>
                                    <td>{formatDate(tournament.date)}</td>
                                    <td>{tournament.size}</td> {/* - tournament.participants.length*/}
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
                                            <>
                                                <Link to={`/tournaments/${tournament.id}/rounds/1`}>
                                                    <button>View Match Details</button>
                                                </Link>
                                                <Link to={`/updateTournament/${tournament.id}`}>
                                                    <button type="submit">Update Tournament</button>
                                                </Link>
                                                <form onSubmit={(e) => handleDelete(e, tournament.id)}>
                                                    <button type="submit">Delete Tournament</button>
                                                </form>
                                            </>
                                        )}
                                    </td>
                                </tr>
                            );})}
                        </tbody>
                    </table>
                </div>
            </main>
        </>
    );
};

export default TournamentManagement;
