// components/UserForm.js
import React from 'react';
import { Link } from 'react-router-dom';
import ErrorMessage from './ErrorMessage';

const UserForm = ({
  title,
  errorMessage,
  onSubmit,
  username,
  password,
  setUsername,
  setPassword,
  submitButtonText,
  linkPath,
  linkText
}) => {
  return (
    <div className="container">
      <ErrorMessage message={errorMessage} />
      <h1>{title}</h1>
      <form onSubmit={onSubmit}>
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

        <input className="submit-button" type="submit" value={submitButtonText} />
      </form>
      <p>
        {linkText} <Link to={linkPath}>{linkPath === "/signup" ? "Signup Here" : "Login Here"}</Link>
      </p>
    </div>
  );
};

export default UserForm;
