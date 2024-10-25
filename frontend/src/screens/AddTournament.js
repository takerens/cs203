import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TournamentForm from '../components/TournamentForm';
import ErrorMessage from '../components/ErrorMessage';

import { handleAddTournament } from '../utils/tournamentUtils';


const AddTournament = () => {
    const [title, setTitle] = useState('');
    const [minElo, setMinElo] = useState('');
    const [maxElo, setMaxElo] = useState('');
    const [date, setDate] = useState('');
    const [size, setSize] = useState('');
    const [totalRounds, setTotalRounds] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();    

    const handleSubmit = async (e) => {
        e.preventDefault();
        const tournamentData = {
            title,
            minElo,
            maxElo,
            date,
            size,
            totalRounds
        };
        handleAddTournament(tournamentData, setErrorMessage, navigate);
    };

    const handleDateChange = (e) => {
        const selectedDate = e.target.value; // Get the date in YYYY-MM-DD format
        const formattedDateTime = `${selectedDate}T00:00:00`; // Append T00:00:00
        setDate(formattedDateTime); // Set the date state
    };

    return (
        <div className="container">
            <h1>Add Tournament</h1>
            <TournamentForm
                title={title}
                setTitle={setTitle}
                minElo={minElo}
                setMinElo={setMinElo}
                maxElo={maxElo}
                setMaxElo={setMaxElo}
                date={date}
                setDate={setDate}
                size={size}
                setSize={setSize}
                totalRounds={totalRounds}
                setTotalRounds={setTotalRounds}
                errorMessage={errorMessage}
                onSubmit={handleSubmit}
                submitButtonText="Add"
                cancelLink="/tournaments"
            />
        </div>
    );
};

export default AddTournament;
