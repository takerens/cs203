// src/components/Login.js
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ErrorMessage from '../components/ErrorMessage';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

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

            if (!response.ok) {
                const errorMessage = await response.text();
                if (response.status === 404) {
                    throw new Error("Login Failed. Try Again.")
                }
                throw new Error(errorMessage);
            }
            // 200 ok -> User Logged In
            navigate('/tournaments');
        } catch (error) {
            setErrorMessage("Login Error: " + error.message);
        }
    };

    return (
        <div className="container">
            <ErrorMessage message={errorMessage}></ErrorMessage>
            <h1>Login</h1>
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

                <input className='login-button' type="submit" value="Login" />
            </form>
            <p>
                Don't have an account? <Link to="/signup">Signup Here</Link>
            </p>
        </div>
    );
};

export default Login;
