import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TournamentForm from '../components/TournamentForm';
import ErrorMessage from '../components/ErrorMessage';
import { handleAddTournament } from '../utils/tournamentUtils';

const AddTournament = () => {
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (tournamentData) => {
        await handleAddTournament(tournamentData, setErrorMessage, navigate);
    };

    return (
        <div className="container">
            <h1>Add Tournament</h1>
            <ErrorMessage message={errorMessage} />
            <TournamentForm onSubmit={handleSubmit} />
        </div>
    );
};

export default AddTournament;
