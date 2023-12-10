import React from "react";
import styles from "./EventList.module.css";

import { mockEvents } from "../../data/events";
import { truncateText } from "../../utils/truncateText";
import { useNavigate } from "react-router-dom";
const EventList: React.FC = () => {
    const navigate = useNavigate();
    
    const onClickCreateEvent = () => {
        navigate('/events/new');
    }

    return (
    <div className={styles.eventListSection}>
      <div className={styles.eventsHeader}>
        <h1 className={styles.eventListTitle}>Your Events</h1>
        <button className={styles.createEventButton} onClick={onClickCreateEvent}>Create New Event</button>
      </div>

      <div className={styles.eventList}>
        {mockEvents.map((event) => (
          <div key={event.id} className={styles.eventCard}>
            <h2 className={styles.eventName}>{truncateText(event.name, 80)}</h2>
            <p className={styles.eventLocation}>
              {truncateText(event.location, 50)}
            </p>
            <p className={styles.eventDescription}>
              {truncateText(event.description, 100)}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};
export default EventList;
