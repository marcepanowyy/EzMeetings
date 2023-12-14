import React,{ChangeEventHandler, useState} from "react";
import styles from "./EventList.module.css";

import { mockEvents } from "../../data/events";
import { truncateText } from "../../utils/truncateText";
import { useNavigate, Link } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";

import { getEvents,participateInEvent } from "../../utils/http";
import { getSimpleToken,getEmailFromToken } from "../../utils/auth";
import LoadingOverlay from "../../ui/LoadingOverlay/LoadingOverlay";

const EventList: React.FC = () => {
  const navigate = useNavigate();
  // useState [participate,setParticipate] with type of input
  const [eventId, setEventId] = useState<string>("");
  
  const onClickCreateEvent = () => {
    navigate("/events/new");
  };

  const token = getSimpleToken();
  const email = getEmailFromToken(token);

  const { data, isLoading, isError, error } = useQuery({
    queryKey: ["events"],
    queryFn: () => getEvents(token as string),
    staleTime: 5000,
  });

  const myEvents = data?.filter(event => event.creatorEmail === email) || [];
  const participatingEvents = data?.filter(event => event.creatorEmail !== email) || [];

  const onParticipateSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      await participateInEvent(eventId, token as string);
      setEventId(""); 
      navigate("/events/" + eventId);
    } catch (err) {
      console.log("error while participating: " + err);
    }
  }
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEventId(e.target.value);
  };
  console.log(JSON.stringify(data));
  return (
    <div className={styles.eventListSection}>
      <form method="post" onSubmit={onParticipateSubmit}  className={styles.searchAndCreateContainer}>
        <input
          className={styles.searchInput}
          type="text"
          id="eventId"
          name="eventId"
          value={eventId} 
          onChange={handleInputChange}
          placeholder="find event by ID"
        />
        <button className={styles.searchButton}>Participate</button>
      </form>
  
      {/* Sekcja "Moje wydarzenia" */}
      <div className={styles.eventList}>
        <div className={styles.wrapper}>
          <h1 className={styles.eventListTitle}>My Events</h1>
          <button
            className={styles.createEventButton}
            onClick={onClickCreateEvent}
          >
            Create New Event
          </button>
        </div>
  
        {isLoading && <LoadingOverlay />}
        {isError && <p>{error}</p>}
        {myEvents.map((event) => (
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
  
      {/* Sekcja "Wydarzenia, w których uczestniczę" */}
      <div className={styles.eventList}>
        <h1 className={styles.eventListTitle}>Events I Participate In</h1>
  
        {participatingEvents.map((event) => (
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

