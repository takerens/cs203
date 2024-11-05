import React from 'react';
import { Link } from 'react-router-dom';
import TableComponent from '../TableComponent';

const TournamentHistory = ({ history }) => {
    if (history.length === 0) return <p>No past tournaments available.</p>;

    const rows = history.map(tournament => [
        tournament.title,
        <Link to={`/tournaments/${tournament.id}/rounds/1`} key={tournament.id}>
            <button>View</button>
        </Link>
    ]);

    return (
        <>
            <h3>Past Tournaments</h3>
            <TableComponent headers={["Title", "View"]} rows={rows} />
        </>
    );
};

export default TournamentHistory;
