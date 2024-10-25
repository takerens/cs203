import React, { useState, useEffect } from 'react';

const DatePicker = ({ value, onChange }) => {
  const [minDate, setMinDate] = useState('');

  // Function to get tomorrow's date in YYYY-MM-DD format
  const getTomorrowDate = () => {
    const today = new Date()
    const tomorrow = new Date();
    tomorrow.setDate(today.getDate() + 1);  // Add 1 day to today's date
    return tomorrow.toISOString().split("T")[0];  // Format the date as YYYY-MM-DD
  };

  // Set the minimum date when page load
  useEffect(() => {
    setMinDate(getTomorrowDate());
  }, []);  // Runs once

  return (
    <input
      type="date"
      value={value} // Controlled input value
      onChange={onChange} // Event handler passed down as a prop
      min={minDate} // Restrict date selection to tomorrow onwards
      required
    />
  );
};

export default DatePicker;
