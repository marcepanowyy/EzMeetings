import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { participateInEvent } from "../../../utils/http";
import useEventsData from "../../../hooks/useEventsData";
import LoadingOverlay from "../../../ui/LoadingOverlay/LoadingOverlay";
import EventSection from "./EventSection";
import { getSimpleToken } from "../../../utils/auth";
import styles from "./EventList.module.css";

const EventList = () => {
  const navigate = useNavigate();
  const [eventId, setEventId] = useState("");
  const { data, isLoading, isError, error, email } = useEventsData();
  const token = getSimpleToken();

  const myEvents = data?.filter((event) => event.creatorEmail === email) || [];
  const participatingEvents =
    data?.filter((event) => event.creatorEmail !== email) || [];

  const onClickCreateEvent = () => navigate("/events/new");

  const onParticipateSubmit = async (e) => {
    e.preventDefault();
    try {
      await participateInEvent(eventId, token);
      setEventId("");
      navigate(`/events/${eventId}`);
    } catch (err) {
      console.log("error while participating: " + err);
    }
  };

  const handleInputChange = (e) => {
    setEventId(e.target.value);
  };

  return (
    <div className={styles.eventListSection}>
      <form
        method="post"
        onSubmit={onParticipateSubmit}
        className={styles.searchAndCreateContainer}
      >
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
      {isError && <p>{error.message}</p>}

      <EventSection title="My Events" events={myEvents} />
      <EventSection
        title="Events I Participate In"
        events={participatingEvents}
      />
    </div>
  );
};

export default EventList;
