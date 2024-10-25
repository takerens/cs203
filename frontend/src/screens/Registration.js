// pages/Registration.js
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import { handleSignup } from '../utils/userUtils';
import UserForm from '../components/UserForm';

const Registration = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault(); // Stop default form submission
    const userData = {
      username,
      password,
      authorities: "ROLE_USER" // Signup only for User
    };
    handleSignup(userData, setErrorMessage, navigate);
  };

  return (
    <UserForm
      title="Account Registration"
      errorMessage={errorMessage}
      onSubmit={handleSubmit}
      username={username}
      password={password}
      setUsername={setUsername}
      setPassword={setPassword}
      submitButtonText="Register"
      linkPath="/login"
      linkText="Already have an account?"
    />
  );
};

export default Registration;
