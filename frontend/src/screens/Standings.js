import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import Navbar from '../components/NavBar';
import SecondaryNavbar from '../components/SecondaryNavbar';

const TournamentStandings = () => {
    const { tournamentId } = useParams();
    const [standings, setStandings] = useState([]);
    const [ tournament, setTournament ] = useState({});
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => { // Fetch Tournament Standings
        const fetchStandings = async () => {
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
                console.log("Tournament Data: " + tournamentData); // View Data for Debugging
                setTournament(tournamentData);
                setStandings(tournamentData.players);
            } catch (error) {
                setErrorMessage("Fetch Tournament Data Error: " + error.message);
            }
        };

        fetchStandings();
    }, [tournamentId]);

    return (
        <><Navbar userRole="ROLE_USER" />
        <SecondaryNavbar userRole="ROLE_USER" tournament={tournament}/>
        <main>
            <h3>Standings</h3>
            <ErrorMessage message={errorMessage}/>
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
                    {standings.map((player, index) => (
                        <tr key={player.username}>
                            <td>{index + 1}</td>
                            <td>{player.username}</td>
                            <td>{player.rating}</td>
                            <td>{player.points}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </main></>
    );
};

export default TournamentStandings;