import React from "react";
import { FaCalendarAlt, FaUser } from "react-icons/fa";
import {
  findCurrentProposalState,
  getStateLabel,
  getClass,
  countVotes,
} from "../../../utils/voteUtils";
import styles from "./EventDetails.module.css";

const ProposalItem = ({ proposal, proposals, handleVote }) => {
  const currentState = findCurrentProposalState(proposals, proposal.id ?? "");
  const stateLabel = getStateLabel(currentState);
  const stateLabelClass = getClass(currentState);
  const voteCounts = countVotes(proposal.votes ?? []);

  const formatDateAndTime = (dateString) => {
    const date = new Date(dateString);
    return `${date.toLocaleDateString()} at ${date.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    })}`;
  };

  return (
    <div className={styles.proposalColumn}>
      <div
        className={`${styles.proposal} ${styles[stateLabelClass]}`}
        onClick={() => handleVote(proposal.id ?? "")}
      >
        <FaCalendarAlt className={styles.icon} />
        <p className={styles.date}>
          Start: {formatDateAndTime(proposal.startDate)}
        </p>
        <p className={styles.proposalStateLabel}>{stateLabel}</p>
      </div>
      <div className={styles.votesContainer}>
        <div className={styles.count}>
          <span className={`${styles.voteCount} ${styles.voteYes}`}>
            YES({voteCounts.YES})
          </span>
          <span className={`${styles.voteCount} ${styles.voteNo}`}>
            NO({voteCounts.NO})
          </span>
          <span className={`${styles.voteCount} ${styles.voteIfNeedBe}`}>
            IF NEED BE({voteCounts.IF_NEED_BE})
          </span>
        </div>
        {proposal.votes &&
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
};

export default ProposalItem;
