import {useParams,useNavigate} from 'react-router-dom'
import React,{useEffect,useState} from 'react';
import EventForm from '../components/event/EventForm';
import { EventResponse } from '../models/api.models';

import {getEventDetails, putEvent} from '../utils/http'
import { getSimpleToken } from '../utils/auth';
import { EventResponseDetails } from '../models/eventDetails.models';
import {useQuery} from '@tanstack/react-query'

import LoadingOverlay from '../ui/LoadingOverlay/LoadingOverlay'
const EditEvent: React.FC = () =>{
    const id = useParams<{ id: string }>().id;
    const token = getSimpleToken();
    const navigate = useNavigate();
   // console.log("WYKONUEJ SIE!!")
    const { data, isLoading, isError, error } = useQuery({
        queryKey: ["events", id],
        queryFn: () => getEventDetails(id as string, token as string),
      });


    const handleEditEvent = async (eventData: EventResponse) => {
      console.log("handleEditEvent",eventData)  
      try {
        const response = await putEvent(eventData, token ?? '');
        navigate(`/events/${response.id}`);
      } catch (error) {
        console.error("Error during event creation:", error);
      }
    };

    let renderComponent = null;
    if (isLoading) renderComponent = <LoadingOverlay />;
    if (isError && error)
    renderComponent =  <p>Error: {error.message}</p>;
    if(data){
        console.log(data)
        renderComponent = <EventForm event={data} onSubmit={handleEditEvent} />;
    }
    return (<>
    {renderComponent}
    </>)
  
 
};

export default EditEvent;
