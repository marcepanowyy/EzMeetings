import React from "react";
import { Link } from "react-router-dom";
import { truncateText } from "../../../utils/truncateText";
import styles from "./EventList.module.css";

const EventSection = ({ title, events }) => (
  <div className={styles.eventList}>
    <h1 className={styles.eventListTitle}>{title}</h1>
    {events.map((event) => (
      <Link
        to={`/events/${event.id}`}
        key={event.id}
        className={styles.eventLink}
      >
        <div className={styles.eventCard}>
          <h2 className={styles.eventName}>{truncateText(event.name, 80)}</h2>
          <p className={styles.eventLocation}>
            {truncateText(event.location, 50)}
          </p>
          <p className={styles.eventDescription}>
            {truncateText(event.description, 100)}
          </p>
        </div>
      </Link>
    ))}
  </div>
);

export default EventSection;
