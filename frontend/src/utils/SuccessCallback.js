export const successCallback = (message, navigate) => {
    return (data) => {
        if (message) {
            alert(message); // Show alert only if message is provided
        }
        if (navigate) {
            navigate(); // Navigate if a navigation function is provided
        }
        return data; // Return the data for further processing if needed
    };
};

export const fetchCallback = (setFunction) => (data) => {
    console.log(`${setFunction.name} Data:`, data);
    setFunction(data);
};

export const getHomePage = (message, navigate) => {
    return successCallback(message, () => navigate('/tournaments'));
};