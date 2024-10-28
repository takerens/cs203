import React from 'react';
import TableComponent from '../TableComponent';

const MatchesTable = ({ matches, editing, newResults, handleChange }) => {
    if (matches.length === 0) return <p>No matches available.</p>;

    const displayResult = (result) => {
        switch (result) {
            case 1: return '1:0';
            case -1: return '0:1';
            case 0.5: return '0.5:0.5';
            default: return 'N/A';
        }
    };

    const rows = matches.map((match, index) => [
        index + 1,
        match.white.username,
        match.white.elo,
        editing && !match.bye ? (
            <select
                value={newResults[match.id] !== undefined ? newResults[match.id] : match.result}
                onChange={(e) => handleChange(match.id, e.target.value)}
            >
                <option value="0.0">N/A</option>
                <option value="1">1:0</option>
                <option value="-1">0:1</option>
                <option value="0.5">0.5:0.5</option>
            </select>
        ) : (
            <span>{displayResult(match.result)}</span>
        ),
        match.black.username,
        match.black.elo
    ]);

    return (
        <TableComponent
            headers={["No.", "White Player", "Rating", "Results", "Black Player", "Rating"]}
            rows={rows}
        />
    );
};

export default MatchesTable;
