import React from 'react';
import { Link } from 'react-router-dom';
import TableComponent from '../TableComponent';

const Tournaments = ({ tournaments, user, handleSubmit }) => {
    if (tournaments.length === 0) return <p>No available tournaments at the moment.</p>;

    const formatDate = (dateString) => {
        const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
        return new Date(dateString).toLocaleDateString('en-GB', options); // Format to dd/mm/yyyy
    };

    const rows = tournaments.map((tournament) => {
        const hasTournamentStarted = new Date() > new Date(tournament.startDateTime);
        const overloaded = tournament.size - tournament.userTournaments.length;
        const vacancies = overloaded < 0 ? 0 : overloaded;

        return [
            tournament.title,
            tournament.minElo,
            tournament.maxElo,
            formatDate(tournament.startDateTime),
            vacancies,
            user.userRole === 'ROLE_USER' ? (
                <>
                    <Link to={`/tournaments/${tournament.id}/rounds/1`}>
                        <button>View Details</button>
                    </Link>
                    <form onSubmit={(e) => handleSubmit(e, tournament.id, "register")}>
                        <button type="submit" disabled={hasTournamentStarted}>Register</button>
                    </form>
                    <form onSubmit={(e) => handleSubmit(e, tournament.id, "withdraw")}>
                        <button type="submit">Withdraw</button>
                    </form>
                </>
            ) : (
                <>
                    <Link to={`/tournaments/${tournament.id}/rounds/1`} className='table-link'>
                        <button type='button' className='table-button'>View Match Details</button>
                    </Link>
                    <Link to={`/updateTournament/${tournament.id}`} className='table-link'>
                        <button type="button" className='table-button'>Update Tournament</button>
                    </Link><form onSubmit={(e) => handleSubmit(e, tournament.id, "delete")}>
                        <button type="submit">Delete Tournament</button>
                    </form>
                </>
            )
        ];
    });

    return (
        <TableComponent headers={["Title", "Min Elo", "Max Elo", "Date", "Vacancies", "Actions"]} rows={rows} />
    );
};

export default Tournaments;