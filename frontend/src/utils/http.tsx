import { QueryClient } from "@tanstack/react-query";
import { redirect, json, ActionFunction } from "react-router-dom";
import { getAuthToken } from "./auth";

import {
    LoginRequest,
    RegisterRequest,
    AuthResponse,
    EventResponse,

} from "../models/api.models";
import { EventResponseDetails } from "../models/eventDetails.models";
import {Vote} from "../models/vote.models";
export const queryClient = new QueryClient();
export const defaultUrl = "http://localhost:8080/";


export const handleResponse = (response: Response) => {
    if (!response.ok) {
      
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const res = response.json();
    console.log("response= ",res);
    return res;
  }
  

  export  const login = async (loginDetails: LoginRequest):Promise<string> => {
    const response = await fetch(defaultUrl+'auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginDetails),
    });

    const resData:AuthResponse = await handleResponse(response);
    const token = resData.token;
    await localStorage.setItem("token", token);
    return token;
  }
  

  export const register = async (registerDetails: RegisterRequest):Promise<string> => {
    const response = await fetch(defaultUrl+'auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(registerDetails),
    });
    const resData:AuthResponse = await handleResponse(response);
    const token = resData.token;
    localStorage.setItem("token", token);
    return token;
  }
  

  export const postNewEvent = async (eventDetails: EventResponse, token: string): Promise<EventResponse> => {
    delete eventDetails.id;
    console.log("postNewEvent", eventDetails);
    console.log("token", token)
    const response = await fetch(defaultUrl+'event', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(eventDetails),
    });
  
    return handleResponse(response);
  }

 
  export const putEvent = async (eventDetails: EventResponse, token: string): Promise<EventResponse> => {
    console.log("putEvent", eventDetails);
    console.log("token", token)
    const response = await fetch(defaultUrl+'event/'+eventDetails.id, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(eventDetails),
    });
  
    return handleResponse(response);
  }
  
  // Fetch function for getting event details
  export const getEventDetails = async (eventId: string, token: string): Promise<EventResponse> => {
    const response = await fetch(`${defaultUrl}event/${eventId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
  
    return handleResponse(response);
  }



  export const getEvents = async (token: string): Promise<EventResponse[]> => {
    const response = await fetch(defaultUrl+'event/all', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
  
    return handleResponse(response);
  }

  export const makeVote = async(token:string,personalVotes:Vote[],eventId:string) => {
    console.log("makeVote:", personalVotes);
    console.log("------------------------")
    const response = await fetch(defaultUrl+'vote/'+eventId,{
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(personalVotes),
    });
    return handleResponse(response);
  }


  export const participateInEvent = async(eventId:string,token:string) => {
    const response = await fetch(defaultUrl+'event/participate/'+eventId,{
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    });
    return handleResponse(response);
  }