import {useParams,useNavigate} from 'react-router-dom'
import React,{useEffect,useState} from 'react';
import EventForm from '../components/event/EventForm/EventForm';
import { EventResponse } from '../models/api.models';

import {getEventDetails, putEvent} from '../utils/http'
import { getSimpleToken } from '../utils/auth';
import { EventResponseDetails } from '../models/eventDetails.models';
import {useQuery,useMutation} from '@tanstack/react-query'
import LoadingOverlay from '../ui/LoadingOverlay/LoadingOverlay'
import { useFeedback } from '../hooks/useFeedback';
const EditEvent: React.FC = () =>{
    const id = useParams<{ id: string }>().id;
    const token = getSimpleToken();
    const navigate = useNavigate();
    const { feedback, showFeedback } = useFeedback();
   // console.log("WYKONUEJ SIE!!")
    const { data, isLoading, isError, error } = useQuery({
        queryKey: ["events", id],
        queryFn: () => getEventDetails(id as string, token as string),
      });

      const {mutate,isPending} = useMutation({
        mutationFn:(eventData: EventResponse)=> putEvent(eventData, token ?? '')
    })

    const handleEditEvent = async (eventData: EventResponse) => {
      mutate(eventData,{
        onSuccess: (response) => {
            showFeedback('success', 'Wydarzenie zostało pomyślnie zaktualizowane!');
        },
        onError: (error) => {
            console.log(error);
            showFeedback('error', error.message);
        }
    }
    )
    };

    let renderComponent = null;
    if (isLoading) renderComponent = <LoadingOverlay />;
    if (isError && error)
    renderComponent =  <p>Error: {error.message}</p>;
    if(data){
        console.log(data)
        renderComponent = <EventForm event={data} isPending={isPending} feedback={feedback} showFeedback={showFeedback} onSubmit={handleEditEvent} editable={true}  />;
    }
    return (<>
    {renderComponent}
    </>)
  
 
};

export default EditEvent;
