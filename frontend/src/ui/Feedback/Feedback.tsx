import React, { useState, useEffect } from 'react';
import { FeedbackMessage, FeedbackType } from '../../models/feedback.model';
import styles from './Feedback.module.css'; // Załóżmy, że stylowanie jest zdefiniowane w pliku Feedback.module.css

interface FeedbackProps {
    feedback: FeedbackMessage | null;
    clearFeedback: () => void;
  }
  
  const Feedback: React.FC<FeedbackProps> = ({ feedback, clearFeedback }) => {
    if (!feedback) return null;
  
    return (
      <div className={`${styles.feedback} ${styles[feedback.type ?? 'vanish']}`}>
        <span>{feedback.message}</span>
        <button onClick={clearFeedback} className={styles.closeButton}>X</button>
      </div>
    );
  };

export default Feedback;
