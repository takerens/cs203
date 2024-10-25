import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import TournamentForm from '../components/TournamentForm';
import { fetchTournamentData, handleUpdateTournament } from '../utils/tournamentUtils';

const UpdateTournament = () => {
    const { tournamentId } = useParams();
    const [tournament, setTournament] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        fetchTournamentData(tournamentId, setErrorMessage, setTournament);
    }, [tournamentId]);

    const handleSubmit = async (updatedTournament) => {
        handleUpdateTournament(updatedTournament, tournamentId, setErrorMessage, navigate);
    };

    return (
        <div className="container">
            <h1>Update Tournament</h1>
            <ErrorMessage message={errorMessage} />
            <TournamentForm
                tournament={tournament}
                onSubmit={handleSubmit}
            />
        </div>
    );
};

export default UpdateTournament;
