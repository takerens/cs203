import React from 'react';
import TableComponent from '../TableComponent';

const StandingsTable = ({ standings, tournament }) => {
    // Helper function to get the userTournament object for a specific user
    const getUserTournament = (userTournaments, tournamentId, username) => {
        return (userTournaments.find(tournament =>
            tournament.id.tournamentId === tournamentId &&
            tournament.id.username === username
        ));
    };

    const rows = standings.map((player, index) => {
        const ut = getUserTournament(player.userTournaments, tournament.id, player.username);
        return [
            index + 1,             // Rank
            player.username,       // Player
            player.elo,           // Rating
            ut.gamePoints         // Points
        ];
    });

    return (
        <TableComponent headers={["Rank", "Player", "Rating", "Points"]} rows={rows} />
    );
};

export default StandingsTable;
