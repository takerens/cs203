import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage'; 

const Registration = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate(); // Initialize useNavigate

  const handleSubmit = async (e) => {
    e.preventDefault(); // Stop default form submission
    setErrorMessage(''); // Clear previous error message

    const userData = {
      username,
      password,
      // authorities: "ROLE_USER", // Account creation only for user
    };

    try {
      const response = await fetch('http://localhost:8080/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData),
      });

      if (!response.ok) {
        const errorResponse = await response.json(); // Get error message from response
        console.error('Trying to Signup:', errorResponse); // Log error for debugging
        throw new Error(errorResponse.message); // General error message
      }

      // User created successfully (201)
      const responseData = await response.json(); // User
      alert(`${responseData.username} has successfully created an account.`); // Success message

      // Redirect to the login page
      navigate('/login');

    } catch (error) {
      setErrorMessage(error.message); // Display error message
    }
  };

  return (
    <div className="container">
      <ErrorMessage message={errorMessage} />
      <h1>Account Registration</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            placeholder="Enter username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoComplete='off'
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            placeholder="Enter password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <input className='register-button' type="submit" value="Register" />
      </form>
      <p>
        Already have an account? <Link to="/login">Login here</Link>
      </p>
    </div>
  );
};

export default Registration;
