import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import Navbar from '../components/Navbar';
import SecondaryNavbar from '../components/SecondaryNavbar';
import { fetchUserData } from '../utils/userUtils';
import { fetchTournamentData, fetchStandings } from '../utils/tournamentUtils';

const TournamentStandings = () => {
    const { tournamentId } = useParams();
    const [standings, setStandings] = useState([]);
    const [tournament, setTournament] = useState({});
    const [errorMessage, setErrorMessage] = useState('');
    const [user, setUser] = useState({});

    useEffect(() => { // Fetch Tournament Standings
        fetchUserData(setErrorMessage, setUser);
        fetchTournamentData(tournamentId, setErrorMessage, setTournament);
        fetchStandings(tournamentId, setErrorMessage, setStandings);
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