import React from 'react';
import { useParams } from 'react-router-dom';
const EditEvent: React.FC = () => {
    const params = useParams();
    return (
        <div>
            <h1>EditEvent</h1>
            <p>{JSON.stringify(params)}</p>
        </div>
    );
};

export default EditEvent;
