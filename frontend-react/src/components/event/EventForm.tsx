import React, { useState, ChangeEvent } from 'react';
import { Calendar, momentLocalizer, SlotInfo, Event as CalendarEvent } from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import styles from './CreateEvent.module.css';
import Proposal from '../../models/proposal.model';


const localizer = momentLocalizer(moment);

const EventForm: React.FC = () => {
  const [proposals, setProposals] = useState<Proposal[]>([]);
  const [name, setName] = useState<string>('');
  const [description, setDescription] = useState<string>('');
  const [location, setLocation] = useState<string>('');

  const isSubmitDisabled = !name || !description || !location || proposals.length === 0;

  const handleSelectSlot = (slotInfo: SlotInfo) => {
    const { start } = slotInfo;
    
    const end = new Date(start);
    end.setHours(start.getHours() + 1);
    if (start < new Date()) {
        return;
      }
    const newProposal: Proposal = {
      id: Math.random(),
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

  return (
    <section className={styles.createEventSection}>
      <h2 className={styles.title}>Create Event</h2>
      <form className={styles.formContainer}>
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
