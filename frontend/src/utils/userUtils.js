import { apiRequest } from './ApiUtils';
import { successCallback, fetchCallback, getHomePage } from './SuccessCallback';

export const handleSignup = async (userData, setErrorMessage, navigate) => {
    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/signup`,
        method: 'POST',
        body: userData,
        callback: successCallback(`${userData.username} has successfully created an account.`, () => navigate('/login')),
        setErrorMessage,
    });
};

export const handleLogin = async (userData, setErrorMessage, navigate) => {
    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/login`,
        method: 'POST',
        body: userData,
        callback: getHomePage(null, navigate),
        setErrorMessage,
    });
};

export const fetchUserData = async (setErrorMessage, setUser) => {
    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/user`,
        method: 'GET',
        callback: fetchCallback(setUser),
        setErrorMessage,
    });
};

export const handlePassword = async (userData, setErrorMessage, setNewPassword) => {
    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/user`,
        method: 'PUT',
        body: userData,
        callback: successCallback('Password changed successfully.', null),
        setErrorMessage,
    }).then(() => {
        setNewPassword(''); // Clear password input after successful password change
    });
};
