import React from 'react';
import EditButton from '../../../ui/Button/EditButton/EditButton';
import CopyButton from '../../../ui/Button/CopyButton/CopyButton';
import styles from './EventDetails.module.css';

const EventActions = ({ isCreator, eventId }) => (
    <div className={styles.creatorActions}>
        {isCreator && <EditButton eventId={eventId} />}
        {isCreator && <CopyButton copyText={eventId} />}
    </div>
);

export default EventActions;
