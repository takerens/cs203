import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TournamentForm from '../components/tournament/TournamentForm';
import ErrorMessage from '../components/ErrorMessage';
import { handleAddTournament } from '../utils/TournamentUtils';

const AddTournament = () => {
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (tournamentData) => {
        await handleAddTournament(tournamentData, setErrorMessage, navigate);
    };

    return (
        <main>
            <div className="view">
                <h1>Add Tournament</h1>
                <ErrorMessage message={errorMessage} />
                <TournamentForm onSubmit={handleSubmit} />
            </div>
        </main>
    );
};

export default AddTournament;