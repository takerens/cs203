import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import UserForm from '../components/UserForm';
import { handleLogin } from '../utils/userUtils';

const Login = () => {

  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (userData) => {
    await handleLogin(userData, setErrorMessage, navigate);
  };

  return (
    <div className='container'>
      <h1>Login</h1>
      <ErrorMessage message={errorMessage} />
      <UserForm
        onSubmit={handleSubmit}
        linkPath="/signup"
      />
    </div>
  );
};

export default Login;
