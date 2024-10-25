import React from 'react';
import { Link } from 'react-router-dom';

const Pagination = ({ tournamentId, inProgressRounds, currentRound, editing }) => {
    return (
        <div className="pagination">
            <span>Rounds:</span>
            {Array.from({ length: inProgressRounds }, (_, index) => { // Generate array from 1 to inProgressRound
                const roundIndex = index + 1; // Start indexing from 1
                return (
                    <Link
                        key={roundIndex}
                        to={`/tournaments/${tournamentId}/rounds/${roundIndex}`}
                        className={
                            roundIndex === Number(currentRound)
                                ? 'current'
                                : editing
                                ? 'unclickable'
                                : ''
                        }
                    >
                        {roundIndex}
                    </Link>
                );
            })}
        </div>
    );
};

export default Pagination;
