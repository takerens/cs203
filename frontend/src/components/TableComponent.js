import React from 'react';

const TableComponent = ({ headers, rows, rowStyles = [] }) => {
    return (
        <table>
            {headers && ( // optional headers
                <thead>
                    <tr>
                        {headers.map((header, index) => (
                            <th key={index}>{header}</th>
                        ))}
                    </tr>
                </thead>
            )}
            <tbody>
                {rows.map((row, rowIndex) => (
                    <tr key={rowIndex} style={rowStyles[rowIndex] || {}}>  {/* Apply row style if it exists */}
                        {row.map((cell, cellIndex) => (
                            <td key={cellIndex}>{cell}</td>
                        ))}
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default TableComponent;
