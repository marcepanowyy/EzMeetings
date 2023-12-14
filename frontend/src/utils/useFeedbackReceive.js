import { useEffect } from 'react';
import { useLocation ,useNavigate } from 'react-router-dom';
import {useFeedback} from './useFeedback';

const useFeedbackReceive = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { feedback, showFeedback } = useFeedback();
  console.log(location);
  const { feedbackType, feedbackMessage } = location.state || {};

  useEffect(() => {
    if (feedbackType && feedbackMessage) {
      showFeedback(feedbackType, feedbackMessage);
        navigate(location.pathname, { replace: true, state: {} });
      
    }
  
  }, [feedbackType, feedbackMessage, showFeedback, navigate, location.pathname]); 

  return { feedback, showFeedback };
};

export default useFeedbackReceive;
