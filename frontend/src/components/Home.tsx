import React from 'react';
import styles from './Home.module.css';
import { useNavigate } from 'react-router-dom';
const Home: React.FC = () => {

    const navigate = useNavigate();
    const handleGetStartedClick = () => {
        navigate('/login');
    };
    return (
    <div className={styles.homeSection}>
      <h1 className={styles.title}>Schedule your Meeting!</h1>
      <button onClick={handleGetStartedClick} className={styles.getStartButton}>Get Started</button>
    </div>
  );
};

export default Home;
