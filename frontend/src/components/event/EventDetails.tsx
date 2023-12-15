import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { useQuery, useMutation } from "@tanstack/react-query";
import { getSimpleToken } from "../../utils/auth";
import { getEventDetails, makeVote } from "../../utils/http";
import styles from "./EventDetails.module.css";
import { FaCalendarAlt, FaUser, FaEdit } from "react-icons/fa";
import { Vote } from "../../models/vote.models";
import {
  proposalVoteHandler,
  findCurrentProposalState,
  getStateLabel,
  countVotes,
  getClass,
} from "../../utils/voteUtils";
import { EventProposal } from "../../models/api.models";
import { MyDecodedToken } from "../../models/MyDecodedToken.model";
import { decodeToken } from "react-jwt";
import Feedback from "../../ui/Feedback/Feedback";
import useFeedbackReceive from "../../utils/useFeedbackReceive";
import { queryClient } from "../../utils/http";
import LoadingOverlay from "../../ui/LoadingOverlay/LoadingOverlay";
const EventDetails: React.FC = () => {
  const { id } = useParams();
  const token = getSimpleToken();
  const { feedback, showFeedback } = useFeedbackReceive();
  const { data, isLoading, isError, error } = useQuery({
    queryKey: ["events", id],
    queryFn: () => getEventDetails(id as string, token as string),
  });

  const { mutate, isPending } = useMutation({
    mutationFn: (votes: Vote[]) => makeVote(token as string, votes),
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
  const EditButton = (
    <div className={styles.editButton}>
      <FaEdit />{" "}
      <Link to="edit">
        <span>Edit Event</span>
      </Link>
    </div>
  );

  const [proposals, setProposals] = useState<Vote[]>([]);

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
    }
  }, [data, email]);

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
    const votesToSubmit = prepareVotesForSubmission(data.eventProposals);
    //console.log("submitVotes", votesToSubmit);
    await mutate(votesToSubmit);
    // window.location.reload();
  };


  return (
    <div className={styles.container}>
      {isPending && <LoadingOverlay />}
      {feedback && (
        <Feedback
          feedback={feedback}
          clearFeedback={() => {
            showFeedback && showFeedback(null, "");
          }}
        />
      )}
      {data && (
        <>
          <h2>{data.name}</h2>
          <p className={styles.location}>Location: {data.location}</p>
          <p className={styles.description}>{data.description}</p>
          {data.creatorEmail == email ? EditButton : null}
          <div className={styles.sliderContainer}>
            {data.eventProposals && data.eventProposals.length > 0 && (
              <div className={styles.proposalsGrid}>
                {data.eventProposals.map((proposal) => {
                  const currentState = findCurrentProposalState(
                    proposals,
                    proposal.id ?? ""
                  );
                  const stateLabel = getStateLabel(currentState);
                  const stateLabelClass = getClass(currentState);
                  const voteCounts = countVotes(proposal.votes ?? []);
                  return (
                    <div key={proposal.id} className={styles.proposalColumn}>
                      <div
                        className={`${styles.proposal} ${styles[stateLabelClass]}`}
                        onClick={() => handleVote(proposal.id ?? "")}
                      >
                        <FaCalendarAlt className={styles.icon} />
                        <p className={styles.date}>
                          Start: {formatDateAndTime(proposal.startDate)}
                        </p>
                        <p className={styles.proposalStateLabel}>
                          {stateLabel}
                        </p>
                      </div>
                      <div className={styles.votesContainer}>
                        <div className={styles.count}>
                          <span
                            className={`${styles.voteCount} ${styles.voteYes}`}
                          >
                            YES({voteCounts.YES})
                          </span>
                          <span
                            className={`${styles.voteCount} ${styles.voteNo}`}
                          >
                            NO({voteCounts.NO})
                          </span>
                          <span
                            className={`${styles.voteCount} ${styles.voteIfNeedBe}`}
                          >
                            IF NEED BE({voteCounts.IF_NEED_BE})
                          </span>
                        </div>

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
          <button className={styles.submitButton} onClick={submitVotes}>
            Submit Votes
          </button>
        </>
      )}
    </div>
  );
};

export default EventDetails;
