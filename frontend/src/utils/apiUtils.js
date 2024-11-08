export const apiRequest = async ({ url, method, body, callback, setErrorMessage }) => {
    setErrorMessage(''); // Clear previous error message

    try {
        // Create headers object
        const headers = { 
            'Content-Type': 'application/json'
        };

        // Check if token exists in localStorage, and add Authorization header if available
        const token = localStorage.getItem('jwtToken');
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        // Prepare the fetch options
        const options = {
            method,
            headers,
            body: body ? JSON.stringify(body) : undefined, // Include body if provided
        };

        const response = await fetch(url, options);

        if (!response.ok) {
            const errorResponse = await response.json();
            console.error(`Error during ${method} at ${url}:`, errorResponse);
            throw new Error(errorResponse.message);
        }

        // Check if the response has a body before parsing
        const responseData = response.status !== 204 ? await response.json() : null;
        callback?.(responseData); // Call success callback if provided
    } catch (error) {
        setErrorMessage(error.message); // Handle error
    }
};
