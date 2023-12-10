import React from "react";
import styles from "./EventList.module.css";

import { mockEvents } from "../../data/events";
import { truncateText } from "../../utils/truncateText";
import { useNavigate ,Link} from "react-router-dom";
import { useQuery } from "@tanstack/react-query";

import { getEvents } from "../../utils/http";
import { getSimpleToken } from "../../utils/auth";
import LoadingOverlay from "../../ui/LoadingOverlay/LoadingOverlay";

const EventList: React.FC = () => {
  const navigate = useNavigate();

  const onClickCreateEvent = () => {
    navigate("/events/new");
  };

  const token = getSimpleToken();
  const { data, isLoading, isError, error } = useQuery({
    queryKey: ["events"],
    queryFn: () => getEvents(token as string),
    staleTime: 5000,
  });

  return (
    <div className={styles.eventListSection}>
      <div className={styles.eventsHeader}>
        <h1 className={styles.eventListTitle}>Your Events</h1>
        <button
          className={styles.createEventButton}
          onClick={onClickCreateEvent}
        >
          Create New Event
        </button>
      </div>

      <div className={styles.eventList}>
        {isLoading && <LoadingOverlay />}
        {isError && <p>{error}</p>}
        {data &&
          data.map((event) => (
            <Link
              to={`/events/${event.id}`}
              key={event.id}
              className={styles.eventLink}
            >
              <div className={styles.eventCard}>
                <h2 className={styles.eventName}>
                  {truncateText(event.name, 80)}
                </h2>
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
    </div>
  );
};
export default EventList;
