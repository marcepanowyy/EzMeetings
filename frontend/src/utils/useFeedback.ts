import { useState, useCallback } from 'react';
import { FeedbackMessage,FeedbackType } from '../models/feedback.model';

export const useFeedback = () => {
  const [feedback, setFeedback] = useState<FeedbackMessage | null>(null);

  const showFeedback = useCallback((type: FeedbackType, message: string) => {
    setFeedback({ type, message });

    setTimeout(() => {
      setFeedback(null);
    }, 3000);
  }, []);

  return { feedback, showFeedback };
};
