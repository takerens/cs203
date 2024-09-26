// src/components/Registration.js
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage'; 
import './Registration.css'

const Registration = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate(); // Initialize useNavigate

  const handleSubmit = async (e) => {
    e.preventDefault(); // stop default form submission
    setErrorMessage(''); // clear error message

    const userData = { // requestbody
      username:username,
      password:password,
      authorities:'ROLE_USER', // only users can create account
    };

    try {
      const response = await fetch('http://localhost:8080/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });

    if (!response.ok) { // 409 Conflict || 400 Bad Request
        const errorMessage = await response.text(); // null
        if (errorMessage == null) {
          errorMessage.setErrorMessage("Username already taken"); // Error from controller not security
        }
        throw new Error(errorMessage);
    }

    // User Created -> 201 
    const responseText = await response.json(); // User Created
    alert(responseText["username"] + " has successfully created an account."); // Handle success message

    // Redirect to the login page
    navigate('/login');

    } catch (error) {
      setErrorMessage(error.message);
      console.error('Error:', error);
    }
  };

  return (
    <div className="container">
      <ErrorMessage message={errorMessage} />
      <h2>User Registration</h2>
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

        <input type="submit" value="Register" />
      </form>
      <p>
        Already have an account? <Link to="/login">Login here</Link>
      </p>
    </div>
  );
};

export default Registration;
