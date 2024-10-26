import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import DatePicker from './DatePicker';
import FormField from './FormField';

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
        <>
            <form onSubmit={handleSubmit}>
                <FormField label="Title" type="text" value={title} setValue={setTitle} />
                <FormField label="Min Elo" type="number" value={minElo} setValue={setMinElo} />
                <FormField label="Max Elo" type="number" value={maxElo} setValue={setMaxElo} />
                <DatePicker value={date} onChange={(e) => setDate(e.target.value)} />
                <FormField label="Size" type="number" value={size} setValue={setSize} />
                <FormField label="Total Rounds" type="number" value={totalRounds} setValue={setTotalRounds} />
                <input className="submit-button" type="submit" value="Submit" />
            </form>
            <p>
                <Link to="/tournaments">Cancel</Link>
            </p>
        </>
    );
};

export default TournamentForm;
