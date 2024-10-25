// pages/Login.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import UserForm from '../components/UserForm';
import { handleLogin } from '../utils/userUtils';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userData = {
            username,
            password,
        };
        handleLogin(userData, setErrorMessage, navigate);
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
