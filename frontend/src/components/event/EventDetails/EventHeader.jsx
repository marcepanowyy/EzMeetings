import React from 'react';
import styles from './EventDetails.module.css';

const EventHeader = ({ name, description, location }) => (
    <div>
        <h2>{name}</h2>
        <p className={styles.location}>Location: {location}</p>
        <p className={styles.description}>{description}</p>
    </div>
);

export default EventHeader;
