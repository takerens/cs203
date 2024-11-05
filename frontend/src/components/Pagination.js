import React from 'react';
import { Link } from 'react-router-dom';

const Pagination = ({ tournamentId, inProgressRounds, currentRound, editing }) => {
    const renderRoundLinks = () => {
        return Array.from({ length: inProgressRounds }, (_, index) => {
            const roundIndex = index + 1; // Start indexing from 1
            const isCurrent = roundIndex === Number(currentRound);
            const isClickable = !editing;

            return (
                <Link
                    key={roundIndex}
                    to={`/tournaments/${tournamentId}/rounds/${roundIndex}`}
                    className={isCurrent ? 'current' : (isClickable ? '' : 'unclickable')}
                >
                    {roundIndex}
                </Link>
            );
        });
    };

    return (
        <div className="pagination">
            <span>Rounds:</span>
            {renderRoundLinks()}
        </div>
    );
};

export default Pagination;
