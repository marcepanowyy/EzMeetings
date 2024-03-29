
  export  interface AuthResponse {
    id: string;
    email: string;
    eventList: any[];
    token: string;
    created: string;
  }
  
  
  
export interface LoginRequest {
    email: string;
    password: string;
  }
  
  export interface RegisterRequest {
    email: string;
    password: string;
  }
  

  export interface EventProposal {
    id?: string;
    startDate: string;
    votes?: Array<{
      voteId: string;
      voterId: string;
      voterEmail: string;
      state: string;
      created: string;
    }>;
  }

  export interface EventVote{
    voteId: string;
    voterId: string;
    voterEmail: string;
    state: string;
    created: string;
  }

  export interface Proposal {
    id?: string;
    start:Date;
      end:Date;
      title:string;
  }
  

  export interface EventResponse {
    id?: string;
    name: string;
    creatorEmail?: string;
    description: string;
    location: string;
    eventProposals?: EventProposal[];
  }
  
  export interface MultipleEventsResponse {
    [index: number]: EventResponse;
  }