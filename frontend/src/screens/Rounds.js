import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Pagination from '../components/Pagination';
import ErrorMessage from '../components/ErrorMessage';
import Navbar from '../components/NavBar';
import SecondaryNavbar from '../components/SecondaryNavbar';

const TournamentRounds = () => {
    const { tournamentId, roundNumber } = useParams();
    const [tournament, setTournament] = useState({});
    const [rounds, setRounds] = useState([]);
    const [matches, setMatches] = useState([]);
    const [user, setUser] = useState({});
    const [editing, setEditing] = useState(false);
    const [newResults, setNewResults] = useState({});
    const [errorMessage, setErrorMessage] = useState('');

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

        // Fetch rounds data
        const fetchRoundsAndMatches = async () => {
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
                setRounds(tournamentData.rounds);
                setMatches(tournamentData.rounds[roundNumber]);
            } catch (error) {
                setErrorMessage("Fetch Tournament Data Error: " + error.message);
            }
        };

        fetchUserData();
        fetchRoundsAndMatches();
    }, [roundNumber, tournamentId]);

    const handleResultChange = async () => {
        try {
            const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}rounds/${roundNumber}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newResults),
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Updating Match Results:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            // Optionally refetch matches or update state accordingly
            const updatedMatches = await response.json();
            setMatches(updatedMatches);
            setEditing(false); // Exit edit mode
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const handleDropdownChange = (matchId, value) => {
        setNewResults((prev) => ({
            ...prev,
            [matchId]: value,
        }));
    };

    const displayResult = (result) => {
        return result === 1 ? '1:0' : result === -1 ? '0:1' : result === 0.5 ? '0.5:0.5' : 'N/A'
    }

    return (
        <><Navbar userRole={user.userRole} />
        <SecondaryNavbar userRole={user.userRole} tournament={tournament}/>
        <main>
            <h3>Round</h3>

            <Pagination tournamentId={tournamentId} inProgressRounds={rounds.length} currentRound={roundNumber} />
            <ErrorMessage message={errorMessage} />
            <div>
                {user.userRole === 'ROLE_ADMIN' && !editing && (
                    <button onClick={() => setEditing(true)}>Edit Match Details</button>
                )}
                {editing && (
                    <button onClick={handleResultChange}>Done</button>
                )}
            </div>

            <table>
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>White Player</th>
                        <th>Rating</th>
                        <th>Points</th>
                        <th>Results</th>
                        <th>Black Player</th>
                        <th>Rating</th>
                        <th>Points</th>
                    </tr>
                </thead>
                <tbody>
                    {matches.map((match, index) => (
                        <tr key={index}>
                            <td>{index + 1}</td>
                            <td>{match.white.username}</td>
                            <td>{match.white.rating}</td>
                            <td>{match.white.points}</td>
                            <td>
                                {editing ? (
                                    <select
                                        defaultValue={displayResult(newResults[match.id]) || displayResult(match.result)}
                                        onChange={(e) => handleDropdownChange(match.id, e.target.value)}
                                    >
                                        <option value="0">N/A</option>
                                        <option value="1">1:0</option>
                                        <option value="-1">0:1</option>
                                        <option value="0.5">0.5:0.5</option>
                                        {/* Option for BYE needed */}
                                    </select>
                                ) : (
                                    <span>
                                        {match.result === 1 ? '1:0' : match.result === -1 ? '0:1' : match.result === 0.5 ? '0.5:0.5' : 'N/A'}
                                    </span>
                                )}
                            </td>
                            <td>{match.black.username}</td>
                            <td>{match.black.rating}</td>
                            <td>{match.black.points}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </main></>
    )
};

export default TournamentRounds;
