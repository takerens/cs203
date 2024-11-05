import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import UserForm from '../components/user/UserForm';
import { handleSignup } from '../utils/UserUtils';

const Registration = () => {
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (userData) => {
    await handleSignup(userData, setErrorMessage, navigate);
  };

  return (
    <main>
    <div className="view">
      <h1>Account Registration</h1>
      <ErrorMessage message={errorMessage} />
      <UserForm onSubmit={handleSubmit} linkPath="/login" />
    </div>
    </main>
  );
};

export default Registration;
