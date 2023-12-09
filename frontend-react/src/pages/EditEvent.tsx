import {useParams,redirect} from 'react-router-dom'
import React,{useEffect,useState} from 'react';
import EventForm from '../components/event/EventForm';
import { EventResponse } from '../models/api.models';

import {getEventDetails} from '../utils/http'
import { getSimpleToken } from '../utils/auth';


const EditEvent: React.FC = () =>{
    const [event, setEvent] = useState<EventResponse>();
    const id = useParams<{ id: string }>().id;
    const token = getSimpleToken();

    useEffect(() => {
        const fetchEvent = async () => {
          try {
            const fetchedEvent = await getEventDetails(id ?? '', token ?? '');
            setEvent(fetchedEvent);
          } catch (error) {
            console.error("Error during event fetching:", error);
          }
        };
    
         fetchEvent();
      }, [id, token]);


    const handleEditEvent = async (eventData: EventResponse) => {
        try {
        } catch (error) {
        console.error("Error during event edition:", error);
        }
    
        redirect('/events');
    };



  return <EventForm event={event} onSubmit={handleEditEvent} />;
};

export default EditEvent;
