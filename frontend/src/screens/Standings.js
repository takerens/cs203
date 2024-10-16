import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import Navbar from '../components/Navbar';
import SecondaryNavbar from '../components/SecondaryNavbar';

const TournamentStandings = () => {
    const { tournamentId } = useParams();
    const [standings, setStandings] = useState([]);
    const [tournament, setTournament] = useState({});
    const [errorMessage, setErrorMessage] = useState('');
    const [user, setUser] = useState({});


    useEffect(() => { // Fetch Tournament Standings
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

        // Fetch Tournamet data
        const fetchTournamentData = async () => {
            try {
                const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                });

                if (!response.ok) {
                    const errorResponse = await response.json(); // Get error message from response
                    console.error('Fetching Tournament Data:', errorResponse); // Log error for debugging
                    throw new Error(errorResponse.message); // General error message
                }

                const tournamentData = await response.json(); // Tournament
                console.log("Tournament Data: " + JSON.stringify(tournamentData, null, 2)); // View Data for Debugging
                setTournament(tournamentData);
            } catch (error) {
                setErrorMessage("Fetch Tournament Data Error: " + error.message);
            }
        }

        const fetchStandings = async () => {
            try {
                const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}/standings`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                });

                if (!response.ok) {
                    const errorResponse = await response.json(); // Get error message from response
                    console.error('Fetching Tournament Data:', errorResponse); // Log error for debugging
                    throw new Error(errorResponse.message); // General error message
                }

                const standingsData = await response.json(); // Tournament
                console.log("Standings Data: " + standingsData); // View Data for Debugging
                setStandings(standingsData);
            } catch (error) {
                setErrorMessage("Fetch Standings Data Error: " + error.message);
            }
        };

        fetchUserData();
        fetchTournamentData();
        fetchStandings();
    }, [tournamentId]);

    const getUserTournament = (userTournaments, tournamentId, username) => {
        return (userTournaments.find(tournament =>
            tournament.id.tournamentId === tournamentId &&
            tournament.id.username === username
        ));
    };

    return (
        <><Navbar userRole={user.userRole} />
            <SecondaryNavbar userRole={user.userRole} tournament={tournament} />
            <main>
                <h3>Standings</h3>
                <ErrorMessage message={errorMessage} />
                <table>
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Player</th>
                            <th>Rating</th>
                            <th>Points</th>
                        </tr>
                    </thead>
                    <tbody>
                        {standings.map((player, index) => {
                            const ut = getUserTournament(player.userTournaments, tournament.id, player.username); // Get user tournament
                            return ( // Return the JSX
                                <tr key={player.username}>
                                    <td>{index + 1}</td>
                                    <td>{player.username}</td>
                                    <td>{player.elo}</td>
                                    <td>{ut.gamePoints}</td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </main></>
    );
};

export default TournamentStandings;