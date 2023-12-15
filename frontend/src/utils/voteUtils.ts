import {Vote} from '../models/vote.models';
import {EventVote} from '../models/api.models';
export const getStateLabel = (currentState: string | null): string => {
    switch (currentState) {
      case 'YES':
        return 'YES (Click here)';
      case 'IF_NEED_BE':
        return 'IF_NEED_BE (Click here)';
      case 'NO':
        return 'NO (Click here)';
      default:
        return 'PENDING (Click here)';
    }
  };

  export const getClass = (currentState: string | null): string => {
    switch (currentState) {
        case 'YES':
            return 'voteYes';
        case 'NO':
            return 'voteNo';
        case 'IF_NEED_BE':
            return 'voteIfNeedBe';
        default:
            return 'labelPending';
    }
};


  export const countVotes = (votes:EventVote[]) => {
    const voteCounts:any = { YES: 0, NO: 0, IF_NEED_BE: 0,PENDING:0 };
    votes.forEach(vote => {
      if (vote.state in voteCounts) {
        voteCounts[vote.state]++;
      }
    });
    return voteCounts;
  };
  
  
  export const getNextState = (currentState: string): string => {
    switch (currentState) {
      case "NO":
        return "PENDING";
      case "PENDING":
        return "YES";
      case "YES":
        return "IF_NEED_BE";
      case "IF_NEED_BE":
        return "NO";
      default:
        return "YES";
    }
  };

  


  export const proposalVoteHandler = (proposals: Vote[], proposalId: string): Vote[] => {
    const existingVoteIndex = proposals.findIndex(vote => vote.proposalId === proposalId);
  
    if (existingVoteIndex === -1) {
      return [...proposals, { proposalId, state: "YES" }];
    } else {
      const updatedVote = { ...proposals[existingVoteIndex], state: getNextState(proposals[existingVoteIndex].state) };
      if (updatedVote.state === "PENDING") {
        return proposals.filter((_, index) => index !== existingVoteIndex);
      } else {
        return proposals.map((vote, index) => index === existingVoteIndex ? updatedVote : vote);
      }
    }
  };

export const findCurrentProposalState = (proposals: Vote[], proposalId: string): string => {
  const currentProposal = proposals.find(p => p.proposalId === proposalId);
  return currentProposal ? currentProposal.state : "PENDING";
};

