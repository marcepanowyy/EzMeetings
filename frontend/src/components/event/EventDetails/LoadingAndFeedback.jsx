import React from 'react';
import LoadingOverlay from '../../../ui/LoadingOverlay/LoadingOverlay';
import Feedback from '../../../ui/Feedback/Feedback';
import styles from './EventDetails.module.css';

const LoadingAndFeedback = ({ isLoading, feedback, showFeedback }) => (
    <>
        {isLoading && <LoadingOverlay />}
        {feedback && (
            <Feedback
                feedback={feedback}
                clearFeedback={() => {
                    showFeedback && showFeedback(null, "");
                }}
            />
        )}
    </>
);

export default LoadingAndFeedback;
