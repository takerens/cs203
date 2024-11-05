export const apiRequest = async ({ url, method, body, callback, setErrorMessage }) => {
    setErrorMessage(''); // Clear previous error message

    try {
        const options = {
            method,
            headers: { 'Content-Type': 'application/json' },
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
