import React, { SetStateAction } from "react";
import { Proposal, EventResponse } from "../models/api.models";
import { formatDateToISO } from "./date.js";
import { SlotInfo, Event as CalendarEvent } from "react-big-calendar";
import { FeedbackType } from "../models/feedback.model";
const calendarUtils = (
  proposals: Proposal[],
  setProposals: React.Dispatch<SetStateAction<Proposal[]>>,
  event?: EventResponse,
  showFeedback?: ((type: FeedbackType, message: string) => void) | undefined,
  editable?: boolean | undefined,
) => {
  const handleSelectSlot = (slotInfo: SlotInfo) => {
    const now = new Date();
    const start = new Date(slotInfo.start);
    if (start < now) {
      return;
    }
    const proposalExists: Proposal | undefined = proposals.find(
      (proposal: Proposal): boolean =>
        formatDateToISO(proposal.start) === formatDateToISO(start),
    );
    if (proposalExists) {
      return;
    }

    const end = new Date(slotInfo.start);
    end.setHours(end.getHours() + 1);
    if (start.getHours() >= 23) {
      return;
    }

    const newProposal: Proposal = {
      id: Math.random().toString(36).substring(7),
      start,
      end,
      title: "New Proposal",
    };
    setProposals([...proposals, newProposal]);
  };

  const handleSelectProposal = (proposal: CalendarEvent) => {
    if (editable) {
      const proposalWithVotes = event?.eventProposals?.find(
        (p) =>
          formatDateToISO(new Date(p.startDate)) ===
          formatDateToISO(proposal?.start),
      );
      if (
        proposalWithVotes &&
        proposalWithVotes.votes &&
        proposalWithVotes.votes.length > 0
      ) {
        showFeedback &&
          showFeedback("error", "Cannot delete a proposal with votes.");
        return;
      }
    }
    setProposals(proposals.filter((e) => e.start !== proposal.start));
  };

  return {
    handleSelectSlot,
    handleSelectProposal,
  };
};

export default calendarUtils;
