import React from "react";
import { FaEdit } from "react-icons/fa";
import { Link } from "react-router-dom";
import styles from "../Button.module.css";
import editStyles from './EditButton.module.css';
interface EditButtonProps {
  eventId?: string;
}

const EditButton: React.FC<EditButtonProps> = ({ eventId }) => {
  return (
    <Link to="edit">
      <div className={styles.button}>
        <FaEdit />
        <span className={editStyles.edit}>Edit</span>
      </div>
    </Link>
  );
};

export default EditButton;
