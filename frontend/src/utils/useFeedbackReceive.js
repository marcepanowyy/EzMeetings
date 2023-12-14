import { useEffect } from 'react';
import { useLocation ,useNavigate } from 'react-router-dom';
import {useFeedback} from './useFeedback';

const useFeedbackReceive = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { feedback, showFeedback } = useFeedback();
  const { feedbackType, feedbackMessage } = location.state || {};

  useEffect(() => {
    if (feedbackType && feedbackMessage) {
      showFeedback(feedbackType, feedbackMessage);


      navigate(location.pathname, { replace: true, state: {} });
    }
  
  }, []); 

  return { feedback, showFeedback };
};

export default useFeedbackReceive;
