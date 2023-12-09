import { QueryClient } from "@tanstack/react-query";
import { redirect, json } from "react-router-dom";
import { getAuthToken } from "./auth";
import {
    LoginRequest,
    RegisterRequest,
    AuthResponse,
    EventCreationRequest,
    EventCreationResponse,
    EventListResponse,
    Event,
} from "../models/api.models";

export const queryClient = new QueryClient();





export const handleResponse = (response: Response) => {
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
  }
  

  export  const login = async (loginDetails: LoginRequest): Promise<AuthResponse> => {
    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginDetails),
    });
  
    return handleResponse(response);
  }
  

  export const register = async (registerDetails: RegisterRequest): Promise<AuthResponse> => {
    const response = await fetch('http://localhost:8080/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(registerDetails),
    });
  
    return handleResponse(response);
  }
  

  export  const createEvent = async (eventDetails: EventCreationRequest, token: string): Promise<EventCreationResponse> => {
    const response = await fetch('http://localhost:8080/event/user', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(eventDetails),
    });
  
    return handleResponse(response);
  }
  

  
  export const fetchEventById = async (eventId: string, token: string): Promise<Event> => {
    const response = await fetch(`http://localhost:8080/event/${eventId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    });
  
    return handleResponse(response);
  }
  
  // Function to get a list of events for a user
export  const fetchUserEvents = async (token: string): Promise<EventListResponse> => {
    const response = await fetch('http://localhost:8080/event/user', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    });
  
    return handleResponse(response);
  }
  