import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import DatePicker from '../components/DatePicker';
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
                    <DatePicker value={date ? date.split('T')[0] : ''} onChange={handleDateChange}/>
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

                <div className="form-group">
                    <label>Total Rounds:</label>
                    <input
                        type="number"
                        id="totalRounds"
                        value={totalRounds}
                        onChange={(e) => setTotalRounds(e.target.value)}
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
