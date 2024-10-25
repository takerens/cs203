import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import DatePicker from '../components/DatePicker';

const UpdateTournament = () => {
    const { tournamentId } = useParams(); // Get the tournament ID from the URL
    const [title, setTitle] = useState('');
    const [minElo, setMinElo] = useState('');
    const [maxElo, setMaxElo] = useState('');
    const [date, setDate] = useState('');
    const [size, setSize] = useState('');
    const [totalRounds, setTotalRounds] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    // Fetch the tournament data when the component mounts or when the tournamentId changes
    useEffect(() => {
        const fetchTournamentData = async () => {
            try {
                const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                });
    
                if (!response.ok) {
                    const errorResponse = await response.json(); // Get error message from response
                    console.error('Fetching Tournament Data:', errorResponse); // Log error for debugging
                    throw new Error(errorResponse.message); // General error message
                }
    
                const tournamentData = await response.json();
                console.log("Tournament Data: " + tournamentData); // View Data for Debugging
                // Set the state with the fetched tournament data
                setTitle(tournamentData.title);
                setMinElo(tournamentData.minElo);
                setMaxElo(tournamentData.maxElo);
                setDate(tournamentData.date);
                setSize(Number(tournamentData.size) - 1);
                setTotalRounds(tournamentData.totalRounds);
            } catch (error) {
                setErrorMessage(error.message);
            }
        };

        fetchTournamentData();
    }, [tournamentId]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        const tournament = {
            title,
            minElo,
            maxElo,
            date,
            size : Number(size) + 1,
            totalRounds
        };

        try {
            const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(tournament),
            });

            if (!response.ok) {
                const errorResponse = await response.json();
                console.error('Trying to Update Tournament:', errorResponse);
                throw new Error(errorResponse.message);
            }

            // Tournament updated successfully
            alert(`You have successfully updated ${title}.`);
            navigate('/tournaments');

        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const handleDateChange = (e) => {
        const selectedDate = e.target.value; // Get the date in YYYY-MM-DD format
        const formattedDateTime = `${selectedDate}T00:00:00`; // Append T00:00:00
        setDate(formattedDateTime); // Set the date state
    };

    return (
        <div className="container">
            <ErrorMessage message={errorMessage} />
            <h1>Update Tournament</h1>
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
                    <DatePicker value={date ? date.split('T')[0] : ''} onChange={(e) => setDate(`${e.target.value}T00:00:00`)}/>
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

                <input className='login-button' type="submit" value="Update" />
            </form>
            <p>
                <Link to="/tournaments">Cancel</Link>
            </p>
        </div>
    );
};

export default UpdateTournament;
