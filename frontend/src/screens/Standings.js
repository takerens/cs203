import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import Navbar from '../components/Navbar';
import SecondaryNavbar from '../components/SecondaryNavbar';
import StandingsTable from '../components/tournament/StandingsTable';
import { fetchUserData } from '../utils/UserUtils';
import { fetchTournamentData, fetchStandings } from '../utils/TournamentUtils';

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

    return (
        <>
            <Navbar userRole={user.userRole} />
            <SecondaryNavbar tournament={tournament} />
            <main>
                <h3>Standings</h3>
                <ErrorMessage message={errorMessage} />
                <StandingsTable standings={standings} tournament={tournament} />
            </main>
        </>
    );
};

export default TournamentStandings;