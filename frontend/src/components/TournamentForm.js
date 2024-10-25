// components/TournamentForm.js
import React from 'react';
import { Link } from 'react-router-dom';
import DatePicker from './DatePicker';
import ErrorMessage from './ErrorMessage';

const TournamentForm = ({
  title,
  setTitle,
  minElo,
  setMinElo,
  maxElo,
  setMaxElo,
  date,
  setDate,
  size,
  setSize,
  totalRounds,
  setTotalRounds,
  errorMessage,
  onSubmit,
  submitButtonText,
  cancelLink
}) => {
  return (
      <><ErrorMessage message={errorMessage} /><form onSubmit={onSubmit}>
          <div className="form-group">
              <label>Title:</label>
              <input
                  type="text"
                  id="title"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  autoComplete="off"
                  required />
          </div>

          <div className="form-group">
              <label>Min Elo:</label>
              <input
                  type="number"
                  id="minElo"
                  value={minElo}
                  onChange={(e) => setMinElo(e.target.value)}
                  required />
          </div>

          <div className="form-group">
              <label>Max Elo:</label>
              <input
                  type="number"
                  id="maxElo"
                  value={maxElo}
                  onChange={(e) => setMaxElo(e.target.value)}
                  required />
          </div>

          <div className="form-group">
              <label>Date:</label>
              <DatePicker value={date ? date.split('T')[0] : ''} onChange={(e) => setDate(`${e.target.value}T00:00:00`)}/>
          </div>

          <div className="form-group">
              <label>Size:</label>
              <input
                  type="number"
                  id="size"
                  value={size}
                  onChange={(e) => setSize(e.target.value)}
                  required />
          </div>

          <div className="form-group">
              <label>Total Rounds:</label>
              <input
                  type="number"
                  id="totalRounds"
                  value={totalRounds}
                  onChange={(e) => setTotalRounds(e.target.value)}
                  required />
          </div>

          <input className="submit-button" type="submit" value={submitButtonText} />
      </form><p>
              <Link to={cancelLink}>Cancel</Link>
          </p></>
  );
};

export default TournamentForm;
