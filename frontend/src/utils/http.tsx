import { QueryClient } from "@tanstack/react-query";
import { redirect, json } from "react-router-dom";
import { getAuthToken } from "./auth";
import {
    LoginRequest,
    RegisterRequest,
    AuthResponse,
    EventResponse,

} from "../models/api.models";

export const queryClient = new QueryClient();

export const handleResponse = (response: Response) => {
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
  }
  

  export  const login = async (loginDetails: LoginRequest) => {
    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginDetails),
    });

    const resData:AuthResponse = await handleResponse(response);
    const token = resData.token;
    localStorage.setItem("token", token);

  }
  

  export const register = async (registerDetails: RegisterRequest) => {
    const response = await fetch('http://localhost:8080/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(registerDetails),
    });
    const resData:AuthResponse = await handleResponse(response);
    const token = resData.token;
    localStorage.setItem("token", token);

  }
  

  export const postNewEvent = async (eventDetails: EventResponse, token: string): Promise<EventResponse> => {
    const response = await fetch('http://localhost:8080/event/user', {
      method: 'POST',
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
    const response = await fetch(`http://localhost:8080/event/${eventId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
  
    return handleResponse(response);
  }