import React from 'react';

const ErrorMessage = ({ message }) => {
  if (!message) return null; // Don't render if there's no message

  return (
    <h3 style={{ color: 'red' }}>
      {message}
    </h3>
  );
};

export default ErrorMessage;
