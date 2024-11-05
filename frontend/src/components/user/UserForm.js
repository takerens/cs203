import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import FormField from '../FormField';

const UserForm = ({ onSubmit, linkPath }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const isLogin = linkPath === "/signup";
  const buttonText = isLogin ? "Login Here" : "Signup Here";
  const linkText = isLogin ? "Signup Here" : "Login Here";
  const authorities = isLogin ? undefined : "ROLE_USER"; // autorities only for signup 

  const handleSubmit = async (e) => {
    e.preventDefault();
    onSubmit({
      username,
      password,
      authorities
    });
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <FormField label="Username: " type="text" value={username} setValue={setUsername} />
        <FormField label="Password: " type="password" value={password} setValue={setPassword} />
        <input className="blue-button" type="submit" value={buttonText} />
      </form>
      <p>
        {isLogin ? "Don't have an account? " : "Already have an account? "}
        <Link to={linkPath}>{linkText}</Link>
      </p>
    </>
  );
};

export default UserForm;
