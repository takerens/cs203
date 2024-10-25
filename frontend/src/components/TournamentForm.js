// components/TournamentForm.js
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import DatePicker from './DatePicker';

const TournamentForm = ({ tournament, onSubmit }) => {
    const [title, setTitle] = useState('');
    const [minElo, setMinElo] = useState('');
    const [maxElo, setMaxElo] = useState('');
    const [date, setDate] = useState('');
    const [size, setSize] = useState('');
    const [totalRounds, setTotalRounds] = useState('');

    // Populate state with tournament values if available
    useEffect(() => {
        if (tournament) {
            setTitle(tournament.title || '');
            setMinElo(tournament.minElo || '');
            setMaxElo(tournament.maxElo || '');
            setDate(tournament.date ? tournament.date.split('T')[0] : '');
            setSize(tournament.size - 1 || '');
            setTotalRounds(tournament.totalRounds || '');
        }
    }, [tournament]);

    const handleSubmit = async (e) => {
        console.log(e);
        e.preventDefault();
        onSubmit({
            title,
            minElo,
            maxElo,
            date: `${date}T00:00:00`, // Format date here
            size: Number(size) + 1,
            totalRounds
        });
    };

    return (
        <><form onSubmit={handleSubmit}>
            <div className="form-group">
                <label>Title:</label>
                <input
                    type="text"
                    id="title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    autoComplete="off"
                    required />
            </div>

            <div className="form-group">
                <label>Min Elo:</label>
                <input
                    type="number"
                    id="minElo"
                    value={minElo}
                    onChange={(e) => setMinElo(e.target.value)}
                    required />
            </div>

            <div className="form-group">
                <label>Max Elo:</label>
                <input
                    type="number"
                    id="maxElo"
                    value={maxElo}
                    onChange={(e) => setMaxElo(e.target.value)}
                    required />
            </div>

            <div className="form-group">
                <label>Date:</label>
                <DatePicker value={date} onChange={(e) => setDate(e.target.value)} />
            </div>

            <div className="form-group">
                <label>Size:</label>
                <input
                    type="number"
                    id="size"
                    value={size}
                    onChange={(e) => setSize(e.target.value - 2)}
                    required />
            </div>

            <div className="form-group">
                <label>Total Rounds:</label>
                <input
                    type="number"
                    id="totalRounds"
                    value={totalRounds}
                    onChange={(e) => setTotalRounds(e.target.value)}
                    required />
            </div>

            <input className="submit-button" type="submit" value="Submit" />
        </form><p>
                <Link to="/tournaments">Cancel</Link>
            </p></>
    );
};

export default TournamentForm;
