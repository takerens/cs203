import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import UserForm from '../components/user/UserForm';
import { handleLogin } from '../utils/UserUtils';

const Login = () => {
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => { // When login page, check if user is already logged in then redirect to home page
    if (localStorage.getItem('jwtToken')) {
      navigate('/tournaments');
    }
}, []);

  const handleSubmit = async (userData) => {
    await handleLogin(userData, setErrorMessage, navigate);
  };

  return (
    <main>
      <div className='view'>
        <h1>Login</h1>
        <ErrorMessage message={errorMessage} />
        <UserForm onSubmit={handleSubmit} linkPath="/signup" />
      </div>
    </main>
  );
};

export default Login;
