import React from "react";
import useEventDetails from "../../../hooks/useEventDetails";
import useUserDetails from "../../../hooks/useUserDetails";
import useProposals from "../../../hooks/useProposals";
import useFeedbackReceive from "../../../hooks/useFeedbackReceive";
import { proposalVoteHandler } from "../../../utils/voteUtils";
import styles from "./EventDetails.module.css";

import EventHeader from "./EventHeader";
import EventActions from "./EventActions";
import ProposalList from "./ProposalList";
import SubmitVotesButton from "./SubmitVotesButton";
import LoadingAndFeedback from "./LoadingAndFeedback";

const EventDetails = () => {
  const { data, isLoading, isError, error, mutate, isPending, id } =
    useEventDetails();
  const email = useUserDetails();
  const { feedback, showFeedback } = useFeedbackReceive();
  const { proposals, setProposals, haveVotesChanged } = useProposals(
    data,
    email,
  );

  const handleVote = (proposalId) => {
    setProposals((currentProposals) =>
      proposalVoteHandler(currentProposals, proposalId),
    );
  };

  const submitVotes = async () => {
    if (!data || !data.eventProposals || !haveVotesChanged()) {
      showFeedback("error", "No changes to the votes were made");
      return;
    }
    const votesToSubmit = proposals
      .filter((proposal) => proposal.state !== "PENDING")
      .map(({ proposalId, state }) => ({ proposalId, state }));

    await mutate(votesToSubmit);
  };

  if (isLoading) return <div className={styles.loading}>Loading...</div>;
  if (isError && error)
    return <p className={styles.errorText}>Error: {error.message}</p>;

  return (
    <div className={styles.container}>
      <LoadingAndFeedback
        isLoading={isPending}
        feedback={feedback}
        showFeedback={showFeedback}
      />
      {data && (
        <>
          <EventHeader
            name={data.name}
            description={data.description}
            location={data.location}
          />
          <EventActions isCreator={data.creatorEmail === email} eventId={id} />
          <ProposalList
            eventProposals={data.eventProposals}
            proposals={proposals}
            handleVote={handleVote}
          />
          <SubmitVotesButton onClick={submitVotes} />
        </>
      )}
    </div>
  );
};

export default EventDetails;
