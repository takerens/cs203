import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Pagination from '../components/Pagination';
import ErrorMessage from '../components/ErrorMessage';
import Navbar from '../components/Navbar';
import SecondaryNavbar from '../components/SecondaryNavbar';
import MatchesTable from '../components/tournament/MatchTable';
import { fetchUserData } from '../utils/UserUtils';
import { fetchTournamentData, fetchRoundsAndMatches, handleUpdateMatchResults } from '../utils/TournamentUtils';

const TournamentRounds = () => {
    const { tournamentId, roundNumber } = useParams();
    const [tournament, setTournament] = useState({});
    const [rounds, setRounds] = useState([]);
    const [matches, setMatches] = useState([]);
    const [user, setUser] = useState({});
    const [editing, setEditing] = useState(false);
    const [newResults, setNewResults] = useState({});
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => { // Fetch user and tournament data when page loads
        fetchUserData(setErrorMessage, setUser);
        fetchTournamentData(tournamentId, setErrorMessage, setTournament);
    }, []);

    useEffect(() => { // Fetch rounds and matches whenever roundNumber changes
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

    return (
        <><Navbar userRole={user.userRole} />
            <SecondaryNavbar tournament={tournament} />
            
            <main>
                <h3>Round</h3>
                <Pagination tournamentId={tournamentId} inProgressRounds={rounds.length} currentRound={roundNumber} editing={editing} />
                <ErrorMessage message={errorMessage} />

                <div>
                    {user.userRole === 'ROLE_ADMIN' && !editing && !rounds[roundNumber - 1]?.over && (
                        <button onClick={() => setEditing(true)}>Edit Match Details</button>
                    )}
                    {editing && (
                        <button onClick={handleResultChange}>Save</button>
                    )}
                </div>

                <MatchesTable matches={matches} editing={editing} newResults={newResults} handleChange={handleDropdownChange} />
            </main>
        </>
    );
};

export default TournamentRounds;