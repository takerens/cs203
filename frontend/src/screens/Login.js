// pages/Login.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import UserForm from '../components/UserForm';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage('');

    const userData = { username, password };

    try {
      const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData),
      });

      if (!response.ok) {
        const errorResponse = await response.json();
        console.error('Trying to Login:', errorResponse);
        throw new Error(errorResponse.message || 'Login failed');
      }

      // Redirect to tournaments page on successful login
      navigate('/tournaments');
    } catch (error) {
      setErrorMessage(error.message);
    }
  };

  return (
    <UserForm
      title="Login"
      errorMessage={errorMessage}
      onSubmit={handleSubmit}
      username={username}
      password={password}
      setUsername={setUsername}
      setPassword={setPassword}
      submitButtonText="Login"
      linkPath="/signup"
      linkText="Don't have an account?"
    />
  );
};

export default Login;
