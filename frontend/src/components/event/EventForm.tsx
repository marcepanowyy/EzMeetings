import React, { useState, ChangeEvent,useEffect } from 'react';
import { Calendar, momentLocalizer, SlotInfo, Event as CalendarEvent } from 'react-big-calendar';


import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import styles from './CreateEvent.module.css';

import {EventResponse,Proposal} from '../../models/api.models';

const localizer = momentLocalizer(moment);

const EventForm: React.FC<{ event?: EventResponse,onSubmit: (eventData: EventResponse) => void; }> = ({ event,onSubmit }) => {
  const [proposals, setProposals] = useState<Proposal[]>([]);
  const [name, setName] = useState<string>(event?.name || '');
  const [description, setDescription] = useState<string>(event?.description || '');
  const [location, setLocation] = useState<string>(event?.location || '');
  const title = event ? "Edit Event" : "Create Event";

  
  useEffect(() => {
    if (event?.eventProposals) {
      const initialProposals = event.eventProposals.map((proposal) => ({
        id: proposal.id,
        start: new Date(proposal.startDate),
        end: new Date(new Date(proposal.startDate).setHours(new Date(proposal.startDate).getHours() + 1)),
        title: event.name
      }));
      setProposals(initialProposals);
    }
  }, [event]);

  const isSubmitDisabled = !name || !description || !location || proposals.length === 0;

  const handleSelectSlot = (slotInfo: SlotInfo) => {
    const start = new Date(slotInfo.start);
    const end = new Date(slotInfo.start);
    end.setHours(end.getHours() + 1);

    const newProposal: Proposal = {
      id:Math.random().toString(36).substring(7),
      start,
      end,
      title: 'New Proposal'
    };

    setProposals([...proposals, newProposal]);
  };

  const handleInputChange = (setter: React.Dispatch<React.SetStateAction<string>>) => (proposal: ChangeEvent<HTMLInputElement>) => {
    setter(proposal.target.value);
  };

  const handleSelectProposal = (proposal: CalendarEvent) => {
    setProposals(proposals.filter(e => e.start !== proposal.start));
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault(); 
    const eventData: EventResponse = {
      id: event?.id || '',
      name,
      description,
      location,
      eventProposals: proposals.map((proposal) => ({
        id: proposal.id,
        startDate: proposal.start.toISOString(),
      }))
    };
    onSubmit(eventData);
  };

  return (
    <section className={styles.createEventSection}>
      <h2 className={styles.title}>{title}</h2>
      <form onSubmit={handleSubmit} className={styles.formContainer}>
        <div className={styles.formHeader}>
        <input
          className={styles.textInput}
          type="text"
          value={name}
          onChange={handleInputChange(setName)}
          placeholder="Name"
        />
        <input
          className={styles.textInput}
          type="text"
          value={description}
          onChange={handleInputChange(setDescription)}
          placeholder="Description"
        />
        <input
          className={styles.textInput}
          type="text"
          value={location}
          onChange={handleInputChange(setLocation)}
          placeholder="Location"
        />
        <p className={styles.hint}>Click on a time slot in the calendar to add a time. Click on a time to remove it.</p>
        </div>
        <div className={styles.calendarContainer}>
          <Calendar
            selectable
            localizer={localizer}
            events={proposals}
            onSelectEvent={handleSelectProposal}
            onSelectSlot={handleSelectSlot}
            views={['week', 'day']} 
            defaultView="week"
            onNavigate={(date) => console.log(date)}
          />
        </div>
        <button 
          className={isSubmitDisabled ? styles.submitButtonDisabled : styles.submitButton} 
          type="submit" 
          disabled={isSubmitDisabled}
        >
          Submit
        </button>
      </form>
    </section>
  );
  
};

export default EventForm;

