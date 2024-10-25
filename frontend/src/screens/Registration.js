// pages/Registration.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import UserForm from '../components/UserForm';

const Registration = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage('');

    const userData = {
      username,
      password,
      authorities: "ROLE_USER", // Signup only for User role
    };

    try {
      const response = await fetch('http://localhost:8080/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData),
      });

      if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(errorResponse.message || 'Registration failed');
      }

      // Success: navigate to the login page
      const responseData = await response.json();
      alert(`${responseData.username} has successfully created an account.`);
      navigate('/login');
    } catch (error) {
      setErrorMessage(error.message);
    }
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
