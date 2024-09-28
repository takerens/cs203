import React, { useEffect, useState, useParams } from 'react';
import ErrorMessage from '../components/ErrorMessage';

const TournamentStandings = () => {
    const { tournamentId } = useParams();
    const [standings, setStandings] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => { // Fetch Tournament Standings
        const fetchStandings = async () => {
            try {
                const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}/standings`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                });
    
                if (!response.ok) {
                    const errorResponse = await response.json(); // Get error message from response
                    console.error('Fetching Tournament Standings:', errorResponse); // Log error for debugging
                    throw new Error(errorResponse.message); // General error message
                }
    
                const tournamentData = await response.json();
                console.log("Tournament Standings: " + tournamentData); // View Data for Debugging
                setStandings(tournamentData); // List of Players
    
            } catch (error) {
                setErrorMessage(error.message);
            }
        };

        fetchStandings();
    }, [tournamentId]);

    return (
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
        </main>
    );
};

export default TournamentStandings;