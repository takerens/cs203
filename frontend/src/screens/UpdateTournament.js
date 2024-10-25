import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import TournamentForm from '../components/TournamentForm';

const UpdateTournament = () => {
    const { tournamentId } = useParams();
    const [title, setTitle] = useState('');
    const [minElo, setMinElo] = useState('');
    const [maxElo, setMaxElo] = useState('');
    const [date, setDate] = useState('');
    const [size, setSize] = useState('');
    const [totalRounds, setTotalRounds] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTournamentData = async () => {
            try {
                const response = await fetch(`http://localhost:8080/tournaments/${tournamentId}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                });

                if (!response.ok) {
                    const errorResponse = await response.json();
                    console.error('Fetching Tournament Data:', errorResponse);
                    throw new Error(errorResponse.message);
                }

                const tournamentData = await response.json();
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
            size: Number(size) + 1,
            totalRounds,
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

            alert(`You have successfully updated ${title}.`);
            navigate('/tournaments');
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    return (
        <div className="container">
            <ErrorMessage message={errorMessage} />
            <h1>Update Tournament</h1>
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
                onSubmit={handleSubmit}
                cancelLink={"/tournaments"}
            />
        </div>
    );
};

export default UpdateTournament;
