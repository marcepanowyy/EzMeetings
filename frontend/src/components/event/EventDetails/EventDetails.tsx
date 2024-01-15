import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { useQuery, useMutation } from "@tanstack/react-query";
import { getSimpleToken } from "../../../utils/auth";
import { getEventDetails, makeVote } from "../../../utils/http";
import styles from "./EventDetails.module.css";
import { FaCalendarAlt, FaUser, FaEdit } from "react-icons/fa";
import { Vote } from "../../../models/vote.models";
import {
  proposalVoteHandler,
  findCurrentProposalState,
  getStateLabel,
  countVotes,
  getClass,
} from "../../../utils/voteUtils";
import { EventProposal } from "../../../models/api.models";
import { MyDecodedToken } from "../../../models/MyDecodedToken.model";
import { decodeToken } from "react-jwt";
import Feedback from "../../../ui/Feedback/Feedback";
import useFeedbackReceive from "../../../hooks/useFeedbackReceive";
import { queryClient } from "../../../utils/http";
import LoadingOverlay from "../../../ui/LoadingOverlay/LoadingOverlay";
import CopyButton from "../../../ui/Button/CopyButton/CopyButton";
import EditButton from "../../../ui/Button/EditButton/EditButton";
import ProposalList from "./ProposalList";
import LoadingAndFeedback from "./LoadingAndFeedback";
import EventHeader from "./EventHeader";
import EventActions from "./EventActions";
import SubmitVotesButton from "./SubmitVotesButton";
const EventDetails: React.FC = () => {
  const { id } = useParams();
  const token = getSimpleToken();
  const { feedback, showFeedback } = useFeedbackReceive();
  const { data, isLoading, isError, error } = useQuery({
    queryKey: ["events", id],
    queryFn: () => getEventDetails(id as string, token as string),
  });

  const { mutate, isPending } = useMutation({
    mutationFn: (votes: Vote[]) => makeVote(token as string, votes,id as string),
    onSuccess: (response) => {
      console.log(response);
      showFeedback("success", "Votes submitted successfully");
      queryClient.invalidateQueries({ queryKey: ["events", id] });
    },
    onError: (error) => {
      console.log(error);
      showFeedback("error", "Something went wrong");
    },
  });

  let email: string | null = null;
  let myDecodedToken: MyDecodedToken | null;
  if (token && typeof token === "string") {
    myDecodedToken = decodeToken<MyDecodedToken>(token);
    email = myDecodedToken?.sub ?? null;
  }
  const EditBtn = (
    <div className={styles.editButton}>
      <EditButton eventId={id as string} />
    </div>
  );

  const CopyBtn = (
    <div className={styles.copyButton}>
      <CopyButton copyText={id as string} />
    </div>
  );

  const [proposals, setProposals] = useState<Vote[]>([]);
  const [originalProposals, setOriginalProposals] = useState<Vote[]>([]);

  useEffect(() => {
    if (data && data.eventProposals) {
      const userProposals = data.eventProposals
          .map((proposal) => {
            const userVote = proposal.votes?.find(
                (vote) => vote.voterEmail === email
            );
            return {
              proposalId: proposal.id ?? "",
              state: userVote ? userVote.state : "PENDING",
            };
          })
          .filter((proposal) => proposal.proposalId);
      setProposals(userProposals as Vote[]);
      setOriginalProposals(userProposals as Vote[]);
    }
  }, [data, email]);


  const haveVotesChanged = () => {
    return JSON.stringify(proposals) !== JSON.stringify(originalProposals);
  };

  const formatDateAndTime = (dateString: string) => {
    const date = new Date(dateString);
    return `${date.toLocaleDateString()} at ${date.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    })}`;
  };

  if (isLoading) return <div className={styles.loading}>Loading...</div>;
  if (isError && error)
    return <p className={styles.errorText}>Error: {error.message}</p>;
  //if (data) console.log(data);
  const handleVote = (proposalId: string) => {
    setProposals((currentProposals) =>
      proposalVoteHandler(currentProposals, proposalId)
    );
  };

  const prepareVotesForSubmission = (
    proposalsFromData: EventProposal[]
  ): Vote[] => {
    return proposals
      .filter((proposal) => proposal.state !== "PENDING")
      .map((proposal) => {
        return {
          proposalId: proposal.proposalId,
          state: proposal.state,
        };
      });
  };

  const submitVotes = async () => {
    if (!data || !data.eventProposals) return;
    if (!haveVotesChanged()) {
      showFeedback("error", "No changes to the votes were made");
      return;
    }
    const votesToSubmit = prepareVotesForSubmission(data.eventProposals);
    await mutate(votesToSubmit);
  };


  return (
      <div className={styles.container}>
        <LoadingAndFeedback isLoading={isPending} feedback={feedback} showFeedback={showFeedback} />
        {data && (
            <>
              <EventHeader name={data.name} description={data.description} location={data.location} />
              <EventActions isCreator={data.creatorEmail === email} eventId={id} />
              <ProposalList eventProposals={data.eventProposals} proposals={proposals} handleVote={handleVote} />
              <SubmitVotesButton onClick={submitVotes} />
            </>
        )}
      </div>
  );
};

export default EventDetails;
