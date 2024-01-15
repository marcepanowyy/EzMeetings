import React from 'react';
import ProposalItem from './ProposalItem';
import styles from './EventDetails.module.css';

const ProposalList = ({ eventProposals, proposals, handleVote }) => (
    <div className={styles.sliderContainer}>
        {eventProposals && eventProposals.length > 0 && (
            <div className={styles.proposalsGrid}>
                {eventProposals.map((proposal) => (
                    <ProposalItem key={proposal.id} proposal={proposal} proposals={proposals} handleVote={handleVote} />
                ))}
            </div>
        )}
    </div>
);

export default ProposalList;
