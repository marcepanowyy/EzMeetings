import React from 'react';
import EventForm from '../components/event/EventForm';
import { EventResponse } from '../models/api.models';

import {
    postNewEvent
} from '../utils/http'
import { getSimpleToken } from '../utils/auth';
import { redirect } from 'react-router-dom';

const CreateEvent: React.FC = () => {
    const token = getSimpleToken();
    const handleCreateEvent = async (eventData: EventResponse) => {
        try {
          const response = await postNewEvent(eventData, token ?? '');
        } catch (error) {
          console.error("Error during event creation:", error);
        }

        redirect('/events');
      };


  return <EventForm onSubmit={handleCreateEvent} />;
};

export default CreateEvent;