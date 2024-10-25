import { apiRequest } from './apiUtils';

export const handleAddTournament = async (tournamentData, setErrorMessage, navigate) => {
    const title = tournamentData.title;

    const onSuccess = () => {
        alert(`You have successfuly created ${title}.`);
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

