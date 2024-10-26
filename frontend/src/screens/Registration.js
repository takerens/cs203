// pages/Registration.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import { handleSignup } from '../utils/userUtils';
import UserForm from '../components/UserForm';

const Registration = () => {
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (userData) => {
    await handleSignup(userData, setErrorMessage, navigate);
  };

  return (
    <div className="container">
      <h1>Account Registration</h1>
      <ErrorMessage message={errorMessage} />
      <UserForm
        onSubmit={handleSubmit}
        linkPath="/login"
      />
    </div>
  );
};

export default Registration;
