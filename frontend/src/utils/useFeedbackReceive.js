import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import {useFeedback} from './useFeedback';

const useFeedbackReceive = () => {
  const location = useLocation();
  const { feedback, showFeedback } = useFeedback();

  useEffect(() => {
    // Odczytanie typu i wiadomości feedbacku z przekazanego stanu
    const { feedbackType, feedbackMessage } = location.state || {};

    if (feedbackType && feedbackMessage) {
      showFeedback(feedbackType, feedbackMessage);
    }
  }, [location.state, showFeedback]);

  return { feedback,showFeedback};
};

export default useFeedbackReceive;
