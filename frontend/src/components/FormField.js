import React from 'react';

const FormField = ({ label, type, value, setValue }) => {
    const handleChange = (e) => {
        setValue(e.target.value); // Use the setter function directly
    };

    return (<div className="form-group">
        
        <label htmlFor={label}>{label}</label>
        <input
            type={type}
            id={label}
            value={value}
            onChange={handleChange}
            autoComplete="off"
            required
        />
    </div>
    );
};

export default FormField;