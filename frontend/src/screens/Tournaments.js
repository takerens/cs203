import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import ErrorMessage from '../components/ErrorMessage';
import TournamentHistory from '../components/tournament/HistoryTable';
import Tournaments from '../components/tournament/TournamentTable';
import { fetchUserData } from '../utils/UserUtils';
import {
    fetchEligibleTournaments, fetchAllTournaments, fetchHistory,
    handleDeleteTournament, handleRegister, handleWithdraw
} from '../utils/TournamentUtils';

const TournamentManagement = () => {
    const [errorMessage, setErrorMessage] = useState('');
    const [tournaments, setTournaments] = useState([]);
    const [history, setHistory] = useState([]);
    const [user, setUser] = useState({});

    useEffect(() => { // When page loaded, fetch user data
        fetchUserData(setErrorMessage, setUser);
    }, []);

    useEffect(() => {
        if (user.userRole === "ROLE_USER") {
            fetchEligibleTournaments(user.elo, setErrorMessage, setTournaments);
            fetchHistory(user.username, setErrorMessage, setHistory);
        } else if (user.userRole === "ROLE_ADMIN") {
            fetchAllTournaments(setErrorMessage, setTournaments);
        } else {
            setErrorMessage('User role not recognized.');
        }
    }, [user])

    const getTitleById = (tournaments, id) => {
        const tournament = tournaments.find(t => t.id === id);
        return tournament ? tournament.title : null; // Return title or null if not found
    };

    const handleSubmit = async (e, tournamentId, action) => {
        e.preventDefault(); // Stop default form submission
        const userData = { username: user.username, elo: user.elo };
        const title = getTitleById(tournaments, tournamentId);

        if (action === 'register') await handleRegister(title, tournamentId, userData, setErrorMessage, setTournaments);
        else if (action === 'withdraw') await handleWithdraw(title, tournamentId, userData, setErrorMessage, setTournaments);
        else await handleDeleteTournament(title, tournamentId, setErrorMessage, setTournaments);
    };

    const renderAdminLink = () => (
        user.userRole === 'ROLE_ADMIN' && (
            <Link to="/addTournament" className="admin-link">
                <button type='button'>New Tournament</button>
            </Link>
        )
    );

    return (
        <>
            <Navbar userRole={user.userRole} />
            <main>
                <h1>Tournament Management</h1>
                <ErrorMessage message={errorMessage} />
                <div className="view">
                    {renderAdminLink()}
                    <h3>Available Tournaments</h3>
                    <Tournaments tournaments={tournaments} user={user} handleSubmit={handleSubmit} />
                    {user.userRole === 'ROLE_USER' && <TournamentHistory history={history} />}
                </div>
            </main>
        </>
    );
};
export default TournamentManagement;