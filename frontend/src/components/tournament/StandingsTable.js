import React from 'react';
import TableComponent from '../TableComponent';

const StandingsTable = ({ standings, tournament, user, handleDeleteUser, handleUnflagUser }) => {
    const getUserTournament = (userTournaments, tournamentId, username) => {
        return (userTournaments.find(tournament =>
            tournament.id.tournamentId === tournamentId &&
            tournament.id.username === username
        ));
    };

    // Define headers based on user role
    const headers = ["Rank", "Player", "Rating", "Points"];
    if (user.userRole === "ROLE_ADMIN") headers.push("Actions");

    const rows = standings.map((player, index) => {
        const ut = getUserTournament(player.userTournaments, tournament.id, player.username);

        // Row data based on role
        const row = [
            index + 1,               // Rank
            player.username,         // Player
            player.elo,              // Rating
            ut.gamePoints            // Points
        ];

        if (user.userRole === "ROLE_ADMIN") {
            row.push(
                player.suspicious ? (
                    <div>
                        <button onClick={() => handleDeleteUser(player)}>Delete</button>
                        <button onClick={() => handleUnflagUser(player)}>Unflag</button>
                    </div>
                ) : null
            );
        }

        return {
            data: row,
            isSuspicious: player.suspicious, // Mark row as suspicious if needed
        };
    });

    return (
        <TableComponent
            headers={headers}
            rows={rows.map(row => row.data)}
            rowStyles={rows.map(row => row.isSuspicious ? { backgroundColor: 'orange' } : {})}
        />
    );
};

export default StandingsTable;
