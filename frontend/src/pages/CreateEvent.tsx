import React from 'react';
import EventForm from '../components/event/EventForm/EventForm';
import { EventResponse } from '../models/api.models';
import {
    postNewEvent
} from '../utils/http'
import { getSimpleToken } from '../utils/auth';
import { useNavigate } from 'react-router-dom';
import {useMutation} from '@tanstack/react-query';
import { useFeedback } from '../hooks/useFeedback';

const CreateEvent: React.FC = () => {
    const token = getSimpleToken();
    const navigate = useNavigate();
    const { feedback, showFeedback } = useFeedback();
    const {mutate,isPending} = useMutation({
        mutationFn:(eventData: EventResponse)=> postNewEvent(eventData,token ?? '')
    })
    const handleCreateEvent = async (eventData: EventResponse) => {
      
          mutate(eventData,{
              onSuccess: (response) => {
                  console.log(response);
                  navigate(`/events/${response.id}`, { 
                    state: { 
                      feedbackType: 'success', 
                      feedbackMessage: 'Event created successfully!'
                    }
                  });
              },
              onError: (error) => {
                  console.log(error);
                  showFeedback('error', error.message);
              }
          }
          )
      }; 
  return <EventForm onSubmit={handleCreateEvent} isPending={isPending} feedback={feedback} showFeedback={showFeedback} />;
};

export default CreateEvent;