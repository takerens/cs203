import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TournamentForm from '../components/TournamentForm';
import ErrorMessage from '../components/ErrorMessage';

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
        setErrorMessage('');

        try {
            const tournamentData = {
                title,
                minElo,
                maxElo,
                date,
                size,
                totalRounds
            };

            const response = await fetch('http://localhost:8080/tournaments', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(tournamentData),
            });

            if (!response.ok) {
                const errorResponse = await response.json();
                throw new Error(errorResponse.message || 'Failed to add tournament');
            }

            alert(`You have successfully created ${title}.`);
            navigate('/tournaments');

        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    // const handleDateChange = (date) => setDate(date ? `${date}T00:00:00` : '');

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
