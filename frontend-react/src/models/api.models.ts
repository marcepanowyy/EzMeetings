
export interface LoginRequest {
    email: string;
    password: string;
  }
  
  export interface RegisterRequest {
    email: string;
    password: string;
  }
  
  export  interface EventCreationRequest {
    name: string;
    description: string;
    location: string;
    eventProposals: Array<{ startDate: string }>;
  }
  
  export  interface AuthResponse {
    id: string;
    email: string;
    eventList: any[];
    token: string;
    created: string;
  }
  
  export interface EventCreationResponse {
    id: string;
    name: string;
    description: string;
    location: string;
    eventProposals: Array<{
      id: string;
      startDate: string;
    }>;
  }
  


  export interface Event {
    id: string;
    name: string;
    description: string;
    location: string;
    eventProposals?: Array<{
      id: string;
      startDate: string;
    }>;
  }
  
  export interface EventListResponse {
    [index: number]: Event; // This assumes the response is an array of events
  }
  