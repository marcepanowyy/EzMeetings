export interface Vote {
    voteId: string;
    voterId: string;
    voterEmail: string;
    state: string;
    created: string;
  }
  
  export interface EventProposal {
    id: string;
    startDate: string;
    votes: Vote[];
  }
  
  export interface EventResponseDetails {
    id: string;
    name: string;
    description: string;
    location: string;
    eventProposals: EventProposal[];
  }
  