import React from 'react';
import styles from './EventDetails.module.css';

const SubmitVotesButton = ({ onClick }) => (
    <button className={styles.submitButton} onClick={onClick}>
        Submit Votes
    </button>
);

export default SubmitVotesButton;