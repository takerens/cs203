import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Pagination from '../components/Pagination';
import ErrorMessage from '../components/ErrorMessage';
import Navbar from '../components/Navbar';
import SecondaryNavbar from '../components/SecondaryNavbar';

const TournamentRounds = () => {
    const { tournamentId, roundNumber } = useParams();
    const [tournament, setTournament] = useState({});
    const [rounds, setRounds] = useState([]);
    const [matches, setMatches] = useState([]);
    const [user, setUser] = useState({});
    const [editing, setEditing] = useState(false);
    const [newResults, setNewResults] = useState({});
    const [loading, setLoading] = useState(true); // Add loading state
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
            } finally {
                setLoading(false);
            }
        }

        fetchUserData();
        fetchTournamentData();
        // fetchRoundsAndMatches();
    }, []);

    useEffect(() => {
        fetchRoundsAndMatches();
    }, [roundNumber]);

    // Fetch rounds data
    const fetchRoundsAndMatches = async () => {
        try {
            const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}/rounds`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Fetching Tournament Data:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            const roundData = await response.json(); // Tournament
            console.log("Round Data: " + JSON.stringify(roundData, null, 2)); // View Data for Debugging
            setRounds(roundData);
            // Check if roundNumber is a valid index
            if (roundData && roundData.length >= roundNumber) {
                setMatches(roundData[roundNumber - 1].matches); // Assuming matches are within each round
            } else {
                // No Matches yet
                setMatches([]); // Or handle this case as needed
            }
        } catch (error) {
            setErrorMessage("Fetch Round Data Error: " + error.message);
        }
    };

    const handleResultChange = async () => {
        try {
            const updatedMatches = matches.map(match => {
                const result = newResults[match.id] !== undefined ? parseFloat(newResults[match.id]) : match.result;
                return {
                    id: match.id,
                    isBYE: result > 1, // Assuming BYE is represented as a result > 1
                    result: result
                };
            });

            const response = await fetch(`http://localhost:8080/match/updateList`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedMatches),
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Updating Match Results:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            fetchRoundsAndMatches();
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
        return result === 1 ? '1:0' : result === -1 ? '0:1' : result === 0.5 ? '0.5:0.5' : result === 2 ? '1:BYE' : result === 3 ? 'BYE:1' : 'N/A'
    }

    if (loading) {
        return <div>Loading...</div>; // Display a loading message while user data is being fetched
    }

    return (
        <><Navbar userRole={user.userRole} />
            <SecondaryNavbar userRole={user.userRole} tournament={tournament} />
            
            <main>
                <h3>Round</h3>

                <Pagination tournamentId={tournamentId} inProgressRounds={rounds.length} currentRound={roundNumber} />
                <ErrorMessage message={errorMessage} />
                <div>
                    {user.userRole === 'ROLE_ADMIN' && !editing && !rounds[roundNumber - 1]?.over &&(
                        <button onClick={() => setEditing(true)}>Edit Match Details</button>
                    )}
                    {editing && (
                        <button onClick={handleResultChange}>Save</button>
                    )}
                </div>

                <table>
                    <thead>
                        <tr>
                            <th>No.</th>
                            <th>White Player</th>
                            <th>Rating</th>
                            <th>Results</th>
                            <th>Black Player</th>
                            <th>Rating</th>
                        </tr>
                    </thead>
                    <tbody>
                        {matches && matches.length > 0 ? (
                            matches.map((match, index) => (
                                <tr key={index}>
                                    <td>{index + 1}</td>
                                    <td>{match.white.username}</td>
                                    <td>{match.white.elo}</td>
                                    <td>
                                        {editing ? (
                                            <select
                                            value={newResults[match.id] !== undefined ? newResults[match.id] : match.result}
                                                onChange={(e) => handleDropdownChange(match.id, e.target.value)}
                                            >
                                                <option value="0.0">N/A</option>
                                                <option value="1">1:0</option>
                                                <option value="-1">0:1</option>
                                                <option value="0.5">0.5:0.5</option>
                                                <option value="2">1:BYE</option>
                                                <option value="3">BYE:1</option>
                                            </select>
                                        ) : (
                                            <span>
                                                {displayResult(match.result)}
                                            </span>
                                        )}
                                    </td>
                                    <td>{match.black.username}</td>
                                    <td>{match.black.elo}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="8">No matches available.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </main></>
    )
};

export default TournamentRounds;
