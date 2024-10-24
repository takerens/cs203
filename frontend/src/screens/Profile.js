import React, { useEffect, useState } from 'react';
import ErrorMessage from '../components/ErrorMessage';
import Navbar from '../components/Navbar';

const Profile = () => {
    const [errorMessage, setErrorMessage] = useState('');
    const [user, setUser] = useState({});
    const [newPassword, setNewPassword] = useState('');

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await fetch("http://localhost:8080/user", { method: 'GET' });

                if (!response.ok) {
                    const errorResponse = await response.json();
                    console.error('Fetching User:', errorResponse);
                    throw new Error(errorResponse.message);
                }

                const userData = await response.json();
                console.log("User Data: ", userData);
                setUser(userData);
            } catch (error) {
                setErrorMessage(error.message);
            }
        };

        fetchUserData(); // Call the fetch function
    }, []);

    const handlePassword = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("http://localhost:8080/user/changePassword", {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username: user.username, password: newPassword, authorities: "ROLE_USER"}),
            });

            if (!response.ok) {
                const errorResponse = await response.json();
                throw new Error(errorResponse.message);
            }

            alert('Password changed successfully!');
            setErrorMessage('');
            setNewPassword(''); // Reset password input
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    return (
        <>
            <Navbar userRole={user.userRole} />
            <main>
                <h1>Profile</h1>
                <ErrorMessage message={errorMessage} />
                {user.username ? (
                    <table>
                        <tbody>
                            <tr>
                                <td><strong>Username:</strong></td>
                                <td>{user.username}</td>
                            </tr>
                            <tr>
                                <td><strong>Change Password:</strong></td>
                                <td>
                                    <form onSubmit={handlePassword}>
                                        <input 
                                            type="text" // New password input
                                            placeholder="Enter New Password" 
                                            value={newPassword} 
                                            onChange={(e) => setNewPassword(e.target.value)} 
                                            required 
                                        />
                                        <button type="submit">Change Password</button>
                                    </form>
                                </td>
                            </tr>
                            <tr>
                                <td><strong>Rating:</strong></td>
                                <td>{user.elo}</td>
                            </tr>
                        </tbody>
                    </table>
                ) : (
                    <p>No user data available.</p>
                )}
            </main>
        </>
    );
};

export default Profile;
