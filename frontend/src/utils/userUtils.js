import { apiRequest } from './apiUtils';

export const handleSignup = async (userData, setErrorMessage, navigate) => {
    const onSuccess = (newUser) => {
        alert(`${newUser.username} has successfully created an account.`)
        navigate('/login') // Redirect to Login Page
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/signup`,
        method: 'POST',
        body: userData,
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const handleLogin = async (userData, setErrorMessage, navigate) => {
    const onSuccess = () => {
        navigate('/tournaments') // Navigate to Home Page
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/login`,
        method: 'POST',
        body: userData,
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const fetchUserData = async (setErrorMessage, setUser) => {
    const onSuccess = (userData) => {
        console.log("User Data: ", userData);
        setUser(userData);
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/user`,
        method: 'GET',
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const handlePassword = async (userData, setErrorMessage, setNewPassword) => {
    const onSuccess = () => {
        alert('Password changed successfully.');
        setNewPassword(''); // Clear password input
    };

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/user`,
        method: 'PUT',
        body: userData,
        successCallback: onSuccess,
        setErrorMessage,
    });
}