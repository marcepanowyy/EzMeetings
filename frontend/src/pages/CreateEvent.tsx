import React from 'react';
import EventForm from '../components/event/EventForm';
import { EventResponse } from '../models/api.models';

import {
    postNewEvent
} from '../utils/http'
import { getSimpleToken } from '../utils/auth';
import { useNavigate } from 'react-router-dom';

const CreateEvent: React.FC = () => {
    const token = getSimpleToken();
    const navigate = useNavigate();
    const handleCreateEvent = async (eventData: EventResponse) => {
        try {
          const response = await postNewEvent(eventData, token ?? '');
          navigate(`/events/${response.id}`);
        } catch (error) {
          console.error("Error during event creation:", error);
        }

        
      };


  return <EventForm onSubmit={handleCreateEvent} />;
};

export default CreateEvent;