import { apiRequest } from './apiUtils';

const createSuccessCallbackWithAlert = (message, navigate) => () => {
    alert(message);
    if (navigate) navigate(); // Navigate if a navigation function is provided
};

export const handleSignup = async (userData, setErrorMessage, navigate) => {
    const onSuccess = createSuccessCallbackWithAlert(`${userData.username} has successfully created an account.`, () => navigate('/login'));

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/signup`,
        method: 'POST',
        body: userData,
        successCallback: onSuccess,
        setErrorMessage,
    });
};

export const handleLogin = async (userData, setErrorMessage, navigate) => {
    const onSuccess = createSuccessCallbackWithAlert('Login successful.', () => navigate('/tournaments'));

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
    const onSuccess = createSuccessCallbackWithAlert('Password changed successfully.', null);

    await apiRequest({
        url: `${process.env.REACT_APP_API_URL}/user`,
        method: 'PUT',
        body: userData,
        successCallback: onSuccess,
        setErrorMessage,
    }).then(() => {
        setNewPassword(''); // Clear password input after successful password change
    });
};
