// src/components/Pagination.js
import React from 'react';
import { Link } from 'react-router-dom';
import './Pagination.css'; // Optional: Import a CSS file for styles

const Pagination = ({ tournamentId, inProgressRounds, currentRound }) => {
    return (
        <div className="pagination">
            <span>Rounds:</span>
            {Array.from({ length: inProgressRounds }, (_, index) => { // Generate array from 1 to inProgressRound
                const roundIndex = index + 1; // Start indexing from 1
                return (
                    <Link
                        key={roundIndex}
                        to={`/tournaments/${tournamentId}/rounds/${roundIndex}`}
                        className={roundIndex === currentRound ? 'current' : ''}
                    >
                        {roundIndex}
                    </Link>
                );
            })}
        </div>
    );
};

export default Pagination;
