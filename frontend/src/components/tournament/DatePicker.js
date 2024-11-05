import React, { useState, useEffect } from 'react';

const DatePicker = ({ value, onChange }) => {
  const [minDate, setMinDate] = useState('');

  useEffect(() => {
    const tomorrowDate = getTomorrowDate();
    setMinDate(tomorrowDate);
  }, []);

  const getTomorrowDate = () => {
    const tomorrow = new Date(Date.now() + 86400000); // Add 1 day in milliseconds
    return tomorrow.toISOString().split("T")[0]; // Format as YYYY-MM-DD
  };

  return (
    <div className="form-group">
      <label>Date: </label>
      <input
        id="Date: "
        type="date"
        value={value}
        onChange={onChange}
        min={minDate} // Restrict to tomorrow
        required
      />
    </div>
  );
};

export default DatePicker;
