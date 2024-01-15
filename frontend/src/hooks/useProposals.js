import { useState, useEffect } from "react";

const useProposals = (data, email) => {
  const [proposals, setProposals] = useState([]);
  const [originalProposals, setOriginalProposals] = useState([]);

  useEffect(() => {
    if (data && data.eventProposals) {
      const userProposals = data.eventProposals
        .map((proposal) => {
          const userVote = proposal.votes?.find(
            (vote) => vote.voterEmail === email,
          );
          return {
            proposalId: proposal.id ?? "",
            state: userVote ? userVote.state : "PENDING",
          };
        })
        .filter((proposal) => proposal.proposalId);

      setProposals(userProposals);
      setOriginalProposals(userProposals);
    }
  }, [data, email]);

  const haveVotesChanged = () => {
    return JSON.stringify(proposals) !== JSON.stringify(originalProposals);
  };

  return { proposals, setProposals, haveVotesChanged };
};

export default useProposals;
