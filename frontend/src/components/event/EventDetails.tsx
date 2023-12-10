import React, { useState } from "react";
import { useParams, Link } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { getSimpleToken } from "../../utils/auth";
import { getEventDetails,makeVote } from "../../utils/http";
import styles from "./EventDetails.module.css";
import { FaCalendarAlt, FaUser, FaEdit } from "react-icons/fa";
import { Vote } from '../../models/vote.models';
import { proposalVoteHandler, findCurrentProposalState,getStateLabel,countVotes } from '../../utils/voteUtils';
import { EventProposal } from "../../models/api.models";
import { MyDecodedToken } from "../../models/MyDecodedToken.model";
import { decodeToken } from "react-jwt";
const EventDetails: React.FC = () => {
  const { id } = useParams();
  const token = getSimpleToken();
  const { data, isLoading, isError, error } = useQuery({
    queryKey: ["events", id],
    queryFn: () => getEventDetails(id as string, token as string),
  });
  let email: string | null = null;
  let myDecodedToken: MyDecodedToken | null;
  if (token && typeof token === "string") {
    myDecodedToken = decodeToken<MyDecodedToken>(token);
    email = myDecodedToken?.sub ?? null;
  }
  
  const [proposals, setProposals] = useState<Vote[]>([]);

  const formatDateAndTime = (dateString: string) => {
    const date = new Date(dateString);
    return `${date.toLocaleDateString()} at ${date.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    })}`;
  };

  if (isLoading) return <div className={styles.loading}>Loading...</div>;
  if (isError && error) return <p className={styles.errorText}>Error: {error.message}</p>;
  if(data) console.log((data));
  const handleVote = (proposalId: string) => {
    setProposals(currentProposals => proposalVoteHandler(currentProposals, proposalId));
  };
  
  const prepareVotesForSubmission = (proposalsFromData: EventProposal[]): Vote[] => {
    return proposalsFromData.map(proposal => {
      const foundProposal = proposals.find(p => p.proposalId === proposal.id);
      return {
        proposalId: proposal.id as string, 
        state: foundProposal ? foundProposal.state : "NO"
      };
    });
  };
  
  const submitVotes = async () => {
    if (!data || !data.eventProposals) return;
    const votesToSubmit = prepareVotesForSubmission(data.eventProposals);
    console.log("submitVotes", votesToSubmit);
    try{
      await makeVote(token as string,votesToSubmit);
     // window.location.reload();
    }catch(error){
      console.log(error);
    } 
  };
  
  return (
    <div className={styles.container}>
      {data && (
        <>
          <h2>{data.name}</h2>
          <p className={styles.location}>Location: {data.location}</p>
          <p className={styles.description}>{data.description}</p>
          <div className={styles.editButton}>
            <FaEdit /> <Link to="edit"><span>Edit Event</span></Link>
          </div>
          <div className={styles.sliderContainer}>
            {data.eventProposals && data.eventProposals.length > 0 && (
              <div className={styles.proposalsGrid}>
                {data.eventProposals.map((proposal) => {
                  const currentState = findCurrentProposalState(proposals, proposal.id ?? "");
                  const stateLabel = getStateLabel(currentState);
                  const voteCounts = countVotes(proposal.votes ?? []);
                  return (
                    <div key={proposal.id} className={styles.proposalColumn}>
                      <div className={styles.proposal} onClick={() => handleVote(proposal.id ?? "")}>
                        <FaCalendarAlt className={styles.icon} />
                        <p className={styles.date}>Start: {formatDateAndTime(proposal.startDate)}</p>
                        <p className={styles.proposalStateLabel}>{stateLabel}</p>
                      </div>  
                      <div className={styles.votesContainer}>
                      <p>YES({voteCounts.YES}) - NO({voteCounts.NO}) - IF NEED BE({voteCounts.IF_NEED_BE})</p>
                      {proposal.votes &&
                        proposal.votes.length > 0 &&
                        proposal.votes.map((vote) => (
                          <div key={vote.voteId} className={styles.vote}>
                            <FaUser className={styles.icon} />
                            <p>{vote.voterEmail}</p>
                            <p>{vote.state}</p>
                          </div>
                        ))}
                    </div>      
                    </div>
                  );
                })}
              </div>
            )}
          </div>
          <button onClick={submitVotes}>Submit Votes</button>
        </>
      )}
    </div>
  );
};

export default EventDetails;
