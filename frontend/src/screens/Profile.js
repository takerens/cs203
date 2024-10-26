import React, { useEffect, useState } from 'react';
import ErrorMessage from '../components/ErrorMessage';
import Navbar from '../components/Navbar';
import FormField from '../components/FormField';
import { fetchUserData, handlePassword } from '../utils/userUtils';

const Profile = () => {
    const [errorMessage, setErrorMessage] = useState('');
    const [user, setUser] = useState({});
    const [newPassword, setNewPassword] = useState('');

    useEffect(() => {
        fetchUserData(setErrorMessage, setUser);
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userData = {
            username: user.username,
            password: newPassword,
            authorities: "ROLE_USER"
        };
        await handlePassword(userData, setErrorMessage, setNewPassword);
    };

    const renderUserInfo = () => (
        <table>
            <tbody>
                <tr>
                    <td><strong>Username:</strong></td>
                    <td>{user.username}</td>
                </tr>
                <tr>
                    <td><strong>Change Password:</strong></td>
                    <td>{renderPasswordChangeForm()}</td>
                </tr>
                <tr>
                    <td><strong>Rating:</strong></td>
                    <td>{user.elo}</td>
                </tr>
            </tbody>
        </table>
    );

    const renderPasswordChangeForm = () => (
        <form onSubmit={handleSubmit}>
            <FormField label="New Password: " type="password" value={newPassword} setValue={setNewPassword} />
            <button type="submit">Change</button>
        </form>
    );

    return (
        <>
            <Navbar userRole={user.userRole} />
            <main>
                <h1>Profile</h1>
                <ErrorMessage message={errorMessage} />
                {user.username ? renderUserInfo() : <p>No user data available.</p>}
            </main>
        </>
    );
};

export default Profile;