import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Pagination from '../components/Pagination';
import ErrorMessage from '../components/ErrorMessage';
import Navbar from '../components/Navbar';
import SecondaryNavbar from '../components/SecondaryNavbar';
import { fetchUserData } from '../utils/userUtils';
import { fetchTournamentData, fetchRoundsAndMatches, handleUpdateMatchResults } from '../utils/tournamentUtils';

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
        fetchUserData(setErrorMessage, setUser);
        fetchTournamentData(tournamentId, setErrorMessage, setTournament);
    }, []);

    useEffect(() => {
        fetchRoundsAndMatches(tournamentId, setErrorMessage, setRounds, setMatches, roundNumber);
    }, [roundNumber]);

    const handleResultChange = async () => {
        const updatedMatches = matches.map(match => ({
            id: match.id,
            bye: match.bye,
            result: newResults[match.id] !== undefined ? parseFloat(newResults[match.id]) : match.result,
        }));
        await handleUpdateMatchResults(updatedMatches, setErrorMessage, setEditing, tournamentId, setRounds, setMatches, roundNumber);
    };

    const handleDropdownChange = (matchId, value) => {
        setNewResults((prev) => ({
            ...prev,
            [matchId]: value,
        }));
    };

    const displayResult = (result) => {
        switch (result) {
            case 1: return '1:0';
            case -1: return '0:1';
            case 0.5: return '0.5:0.5';
            default: return 'N/A';
        }
    };

    return (
        <><Navbar userRole={user.userRole} />
            <SecondaryNavbar tournament={tournament} />
            
            <main>
                <h3>Round</h3>
                <Pagination tournamentId={tournamentId} inProgressRounds={rounds.length} currentRound={roundNumber} editing={editing} />
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
                        {matches.length > 0 ? (
                            matches.map((match, index) => (
                                <tr key={match.id}>
                                    <td>{index + 1}</td>
                                    <td>{match.white.username}</td>
                                    <td>{match.white.elo}</td>
                                    <td>
                                        {editing && !match.bye ? (
                                            <select
                                            value={newResults[match.id] !== undefined ? newResults[match.id] : match.result}
                                                onChange={(e) => handleDropdownChange(match.id, e.target.value)}
                                            >
                                                <option value="0.0">N/A</option>
                                                <option value="1">1:0</option>
                                                <option value="-1">0:1</option>
                                                <option value="0.5">0.5:0.5</option>
                                            </select>
                                        ) : (
                                            <span>{displayResult(match.result)}</span>
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
            </main>
        </>
    );
};

export default TournamentRounds;
