// src/components/Message.js
import React from 'react';

const Message = ({ message }) => {
    if (!message) return null; // Don't render anything if there's no message

    return (
        <h3 style={{ color: 'green' }}>
            {message}
        </h3>
    );
};

export default Message;