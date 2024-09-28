import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';

const AddTournament = () => {
    const [title, setTitle] = useState('');
    const [minElo, setMinElo] = useState('');
    const [maxElo, setMaxElo] = useState('');
    const [date, setDate] = useState('');
    const [size, setSize] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        const tournamentData = {
            title,
            minElo,
            maxElo,
            date,
            size,
        };

        try {
            const response = await fetch('http://localhost:8080/tournaments', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(tournamentData),
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Trying to Add Tournament:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            // Tournament added successfully
            alert(`You have successfuly created ${title}.`)
            navigate('/tournaments');

        } catch (error) {
            setErrorMessage(error.message); // Display error message
        }
    };

    return (
        <div className="container">
            <ErrorMessage message={errorMessage} />
            <h1>Add Tournament</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Title:</label>
                    <input
                        type="text"
                        id="title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        autoComplete="off"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Min Elo:</label>
                    <input
                        type="number"
                        id="minElo"
                        value={minElo}
                        onChange={(e) => setMinElo(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Max Elo:</label>
                    <input
                        type="number"
                        id="maxElo"
                        value={maxElo}
                        onChange={(e) => setMaxElo(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Date:</label>
                    <input
                        type="date"
                        id="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Size:</label>
                    <input
                        type="number"
                        id="size"
                        value={size}
                        onChange={(e) => setSize(e.target.value)}
                        required
                    />
                </div>

                <input className='login-button' type="submit" value="Add" />
            </form>
            <p>
                <Link to="/tournaments">Cancel</Link>
            </p>
        </div>
    );
};

export default AddTournament;
