// src/components/Login.js
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';
import './Login.css'; // Create a separate CSS file for styles if needed

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        const userData = {
            username:username,
            password:password,
        };

        try {
            const response = await fetch('http://localhost:8080/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData),
            });

            const responseText = await response.text();

            if (!response.ok) {
                throw new Error(responseText);
            }
            alert("Login succesful");

            // navigate('/index');
        } catch (error) {
            setErrorMessage(error.message);
            console.error('Error:', error);
        }
    };

    return (
        <div className="container">
            <ErrorMessage message={errorMessage}></ErrorMessage>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Username:</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        autoComplete="off"
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>

                <input type="submit" value="Login" />
            </form>
            <p>
                Don't have an account? <Link to="/signup">Signup Here</Link>
            </p>
        </div>
    );
};

export default Login;
