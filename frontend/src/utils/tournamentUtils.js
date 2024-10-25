import { apiRequest } from './apiUtils';

const getTournamentTitleById = (tournaments, id) => {
    const tournament = tournaments.find(t => t.id === id);
    return tournament ? tournament.title : null; // Return title or null if not found
};

export const handleAddTournament = async (tournamentData, setErrorMessage, navigate) => {
    const onSuccess = () => {
        alert(`You have successfuly created ${tournamentData.title}.`);
        navigate('/tournaments') // Return to Home Page
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments`,
        method: 'POST',
        body: tournamentData,
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const handleUpdateTournament = async (tournamentData, tournamentId, setErrorMessage, navigate) => {
    const onSuccess = () => {
        alert(`You have successfuly updated ${tournamentData.title}.`);
        navigate('/tournaments') // Return to Home Page
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}`,
        method: 'PUT',
        body: tournamentData,
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const handleUpdateMatchResults = async (updatedMatches, setErrorMessage, setEditing, tournamentId, setRounds, setMatches, roundNumber) => {
    const onSuccess = () => {
        fetchRoundsAndMatches(tournamentId, setErrorMessage, setRounds, setMatches, roundNumber);
        setEditing(false); // Exit edit mode
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/match/updateList`,
        method: 'PUT',
        body: updatedMatches,
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const fetchTournamentData = async (tournamentId, setErrorMessage, setTournament) => {
    const onSuccess = (tournamentData) => {
        console.log("Tournament Data: " + JSON.stringify(tournamentData, null, 2));
        setTournament(tournamentData);
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}`,
        method: 'GET',
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const fetchRoundsAndMatches = async (tournamentId, setErrorMessage, setRounds, setMatches, roundNumber) => {
    const onSuccess = (roundData) => {
        console.log("Round Data: " + JSON.stringify(roundData, null, 2));
        setRounds(roundData);
        
        if (roundData && roundData.length >= roundNumber) { // Check if roundNumber is a valid index
            setMatches(roundData[roundNumber - 1].matches);
        } else { // No Matches yet
            setMatches([]);
        }
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}/rounds`,
        method: 'GET',
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const fetchStandings = async (tournamentId, setErrorMessage, setStandings) => {
    const onSuccess = (standingsData) => {
        console.log("Standings Data: " + standingsData); // View Data for Debugging
        setStandings(standingsData);
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}/standings`,
        method: 'GET',
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const fetchEligibleTournaments = async (elo, setErrorMessage, setTournaments) => {
    const onSuccess = (tournamentsData) => {
        console.log("Eligible Data: " + tournamentsData); // View Data for Debugging
        setTournaments(tournamentsData);
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/byElo/${elo}`,
        method: 'GET',
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const fetchHistory = async (username, setErrorMessage, setHistory) => {
    const onSuccess = (tournamentsData) => {
        console.log("History Data: " + tournamentsData); // View Data for Debugging
        setHistory(tournamentsData);
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/byUser/${username}`,
        method: 'GET',
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const fetchAllTournaments = async (setErrorMessage, setTournaments) => {
    const onSuccess = (tournamentsData) => {
        console.log("All Tournament Data: " + tournamentsData); // View Data for Debugging
        setTournaments(tournamentsData);
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments`,
        method: 'GET',
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const handleRegister = async (tournaments, tournamentId, userData, setErrorMessage, setTournaments) => {
    const onSuccess = () => {
        alert(`${userData.username} has registered for ${getTournamentTitleById(tournaments, tournamentId)}.`); // Success message
        fetchEligibleTournaments(userData.elo, setErrorMessage, setTournaments); // Refresh Page
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}/register`,
        method: 'POST',
        body: userData,
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const handleWithdraw = async (tournaments, tournamentId, userData, setErrorMessage, setTournaments) => {
    const onSuccess = () => {
        alert(`${userData.username} has withdrawn from ${getTournamentTitleById(tournaments, tournamentId)}.`); // Success message
        fetchEligibleTournaments(userData.elo, setErrorMessage, setTournaments); // Refresh Page
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}/withdraw`,
        method: 'DELETE',
        body: userData,
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const handleDeleteTournament = async (tournaments, tournamentId, setErrorMessage, setTournaments) => {
    const onSuccess = () => {
        alert(`${getTournamentTitleById(tournaments, tournamentId)} has been deleted.`); // Success message
        fetchAllTournaments(setErrorMessage, setTournaments); // Refresh Page
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}`,
        method: 'DELETE',
        successCallback: onSuccess,
        setErrorMessage,
    });
};
