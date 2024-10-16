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
            username,
            password,
        };

        try {
            const response = await fetch('http://spring-app:8080/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            if (!response.ok) {
                const errorResponse = await response.json(); // Get error message from response
                console.error('Trying to Login:', errorResponse); // Log error for debugging
                throw new Error(errorResponse.message); // General error message
            }

            // User login successfully (200)
            // Redirect to the login page
            navigate('/tournaments');

        } catch (error) {
            setErrorMessage(error.message); // Display error message
        }
    };

    return (
        <div className="container">
            <ErrorMessage message={errorMessage} />
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
