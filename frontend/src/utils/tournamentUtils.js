import { apiRequest } from './apiUtils';

const getTournamentTitleById = (tournaments, id) => {
    const tournament = tournaments.find(t => t.id === id);
    return tournament ? tournament.title : null; // Return title or null if not found
};

const createSuccessCallback = (message, navigate) => () => {
    alert(message);
    navigate('/tournaments'); // Return to Home Page
};

const createFetchCallback = (setFunction) => (data) => {
    console.log(`${setFunction.name} Data:`, data);
    setFunction(data);
};

export const handleAddTournament = async (tournamentData, setErrorMessage, navigate) => {
    const onSuccess = createSuccessCallback(`You have successfully created ${tournamentData.title}.`, navigate);

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments`,
        method: 'POST',
        body: tournamentData,
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const handleUpdateTournament = async (tournamentData, tournamentId, setErrorMessage, navigate) => {
    const onSuccess = createSuccessCallback(`You have successfully updated ${tournamentData.title}.`, navigate);

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
    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}`,
        method: 'GET',
        successCallback: createFetchCallback(setTournament),
        setErrorMessage,
    });
};

export const fetchRoundsAndMatches = async (tournamentId, setErrorMessage, setRounds, setMatches, roundNumber) => {
    const onSuccess = (roundData) => {
        console.log("Round Data:", roundData);
        setRounds(roundData);
        setMatches(roundData?.[roundNumber - 1]?.matches || []); // Set matches or empty array
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}/rounds`,
        method: 'GET',
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const fetchStandings = async (tournamentId, setErrorMessage, setStandings) => {
    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}/standings`,
        method: 'GET',
        successCallback: createFetchCallback(setStandings),
        setErrorMessage,
    });
};

export const fetchEligibleTournaments = async (elo, setErrorMessage, setTournaments) => {
    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/byElo/${elo}`,
        method: 'GET',
        successCallback: createFetchCallback(setTournaments),
        setErrorMessage,
    });
};

export const fetchHistory = async (username, setErrorMessage, setHistory) => {
    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/byUser/${username}`,
        method: 'GET',
        successCallback: createFetchCallback(setHistory),
        setErrorMessage,
    });
};

export const fetchAllTournaments = async (setErrorMessage, setTournaments) => {
    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments`,
        method: 'GET',
        successCallback: createFetchCallback(setTournaments),
        setErrorMessage,
    });
};

const createRegistrationCallback = (message, tournaments, tournamentId, userData, setErrorMessage, setTournaments) => () => {
    alert(message);
    fetchEligibleTournaments(userData.elo, setErrorMessage, setTournaments); // Refresh Page
};

export const handleRegister = async (tournaments, tournamentId, userData, setErrorMessage, setTournaments) => {
    const onSuccess = createRegistrationCallback(`${userData.username} has registered for ${getTournamentTitleById(tournaments, tournamentId)}.`, tournaments, tournamentId, userData, setErrorMessage, setTournaments);

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/tournaments/${tournamentId}/register`,
        method: 'POST',
        body: userData,
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const handleWithdraw = async (tournaments, tournamentId, userData, setErrorMessage, setTournaments) => {
    const onSuccess = createRegistrationCallback(`${userData.username} has withdrawn from ${getTournamentTitleById(tournaments, tournamentId)}.`, tournaments, tournamentId, userData, setErrorMessage, setTournaments);

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
