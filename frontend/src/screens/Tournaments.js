import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/NavBar';
import ErrorMessage from '../components/ErrorMessage';

const TournamentManagement = () => {
    const [errorMessage, setErrorMessage] = useState('');
    const [tournaments, setTournaments] = useState([]);
    const [user, setUser] = useState({});
    const [credentials, setCredentials] = useState('');

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                // Fetch User data
                const userResponse = await fetch("http://localhost:8080/user", {
                    method: 'GET',
                });

                if (!userResponse.ok) {
                    throw new Error("Failed to fetch user.");
                }

                const userData = await userResponse.json();
                setUser(userData);
            } catch (error) {
                setErrorMessage(error.message);
            }
        };

        fetchUserData();
    }, []); // Runs only once

    useEffect(() => { // after successfully getting userdata
        if (user.username && user.password) {
            const fetchTournamentData = async () => {
                try {
                    // Encode the credentials for Basic Authentication
                    const credentials = btoa(`${user.username}:user1234`); // PASSWORD HARDCODED FOR NOW
                    setCredentials(credentials);

                    // Fetch Tournaments Data
                    const response = await fetch("http://localhost:8080/tournaments", {
                        method: 'GET',
                        headers: {
                            'Authorization': `Basic ${credentials}`, // If you have a token to include
                            'Content-Type': 'application/json',
                        },
                    });
        
                    if (!response.ok) {
                        const errorDetails = await response.text(); // Get more details about the error
                        throw new Error(`Failed to fetch tournaments: ${errorDetails}`);
                    }
            
                    // Ensure the response is valid JSON
                    const tournamentData = await response.json();
                    setTournaments(tournamentData);
                } catch (error) {
                    setErrorMessage(error.message);
                }
            };
        
            fetchTournamentData();
        }
    }, [user]);

    const formatDate = (dateString) => {
        const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
        return new Date(dateString).toLocaleDateString('en-GB', options); // 'en-GB' for dd/mm/yyyy format
    };

    const handleRegister = async (e, tournamentId) => {
        e.preventDefault();
        setErrorMessage('');

        try {
            const response = await fetch(`http://localhost:8080/register/${tournamentId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Basic ${credentials}`, // If you have a token to include
                    'Content-Type': 'application/json',
                }, 
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                throw new Error(errorMessage);
            }

            // Succeful registration
            alert('Successfully Registered for Tournament.');
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const handleWithdraw = async (e, tournamentId) => {
        e.preventDefault();
        setErrorMessage('');

        try {
            const response = await fetch(`http://localhost:8080/withdraw/${tournamentId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Basic ${credentials}`, // If you have a token to include
                    'Content-Type': 'application/json',
                }, 
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                throw new Error(errorMessage);
            }

            // Succeful registration
            alert('Successfully Withdrawn from Tournament.');
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    return (
        <><Navbar userRole={ user.userRole } />
        <main>
            <ErrorMessage message={errorMessage} />
            <h1>Tournament Management</h1>
            
            <div className="view">
                <p>Available Tournaments.</p>
                { user.userRole === 'ROLE_ADMIN' && (
                <p>Add New Tournament: <form onSubmit={`/tournaments`}>
                                                <button type="submit">Register</button>
                                        </form></p>
                ) }
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
                                <td>{formatDate(tournament.date)}</td>
                                <td>{tournament.size - tournament.participants.length}</td>
                                <td>
                                    { user.userRole === 'ROLE_USER' && (
                                        <><Link to={`/tournaments/${tournament.id}`}>
                                            <button>View Details</button>
                                        </Link><form onSubmit={(e) => handleRegister(e, tournament.id)}>
                                                <button type="submit">Register</button>
                                        </form><form onSubmit={(e) => handleWithdraw(e, tournament.id)}>
                                            <button type="submit">Withdraw</button>
                                        </form></>
                                    ) }
                                    { user.userRole === 'ROLE_ADMIN' && (
                                        <><Link to={`/tournaments/${tournament.id}/1`}>
                                            <button>Update Match Details</button>
                                        </Link><form action={`/tournaments/${tournament.id}`} method="post">
                                                <button type="submit">Update Tournament</button>
                                        </form><form action={`/tournaments/${tournament.id}`} method="post">
                                            <button type="submit">Delete Tournament</button>
                                        </form></>
                                    ) }
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </main>
        </>
    );
};

export default TournamentManagement;
