export const apiRequest = async ({ url, method, body, successCallback, setErrorMessage }) => {
    setErrorMessage(''); // Clear previous error message
    
    try {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: body ? JSON.stringify(body) : undefined, // Include body if provided
        });
    
        if (!response.ok) {
            const errorResponse = await response.json();
            console.error(`Trying to ${method} at ${url}:`, errorResponse);
            throw new Error(errorResponse.message);
        }

        // Check if the response has a body before parsing
        const responseData = response.status !== 204 ? await response.json() : null;
        if (successCallback) {
            successCallback(responseData); // Call the success callback function
        }
    } catch (error) {
        setErrorMessage(error.message); // Handle error
    }
};