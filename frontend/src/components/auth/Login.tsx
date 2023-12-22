import React from "react";
import styles from "./AuthForms.module.css";
import { emailValidator, passwordValidator } from "../../utils/validators";
import useInput from "../../hooks/use-input";
import {Link, redirect,useNavigate} from 'react-router-dom';

import { login } from '../../utils/http';
import { LoginRequest } from '../../models/api.models'; 


import { useFeedback } from '../../hooks/useFeedback';
import Feedback from "../../ui/Feedback/Feedback";
import {useMutation} from '@tanstack/react-query';
import LoadingOverlay from "../../ui/LoadingOverlay/LoadingOverlay";

const Login: React.FC = () => {
  const navigate = useNavigate();
  const { feedback, showFeedback } = useFeedback();
  const {mutate,isPending} = useMutation({
    mutationFn:(loginDetails: LoginRequest)=> login(loginDetails)
  })


  const {
    value: emailValue,
    isValid: emailIsValid,
    hasError: emailHasError,
    valueChangeHandler: emailChangeHandler,
    inputBlurHandler: emailBlurHandler,
    reset: resetEmail,
  } = useInput(emailValidator);

  const {
    value: passwordValue,
    isValid: passwordIsValid,
    hasError: passwordHasError,
    valueChangeHandler: passwordChangeHandler,
    inputBlurHandler: passwordBlurHandler,
    reset: resetPassword,
  } = useInput(passwordValidator);

  const formSubmitHandler = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!emailIsValid || !passwordIsValid) {
      return;
    }
    const loginDetails: LoginRequest = {
      email: emailValue,
      password: passwordValue,
    }; 
    
      mutate(loginDetails,{
        onSuccess: (response) => {
            console.log(response);
            navigate('/', { 
              state: { 
                feedbackType: 'success', 
                feedbackMessage: 'Logged in successfully!',
              }
            });
        },
        onError: (error) => {
          console.log("MY ERROR!!!!!",error.stack); 
         // showFeedback('error', error); 
        }
      });
  };
  



  return (
    <section className={styles.authSection}>
      {isPending && <LoadingOverlay />}
      {feedback && (
        <Feedback
          feedback={feedback}
          clearFeedback={() => {
            showFeedback && showFeedback(null, "");
          }}
        />
      )}
      <div className={styles.authFormSection}>
        <h1 className={styles.authTitle}>Login</h1>
        <form onSubmit={formSubmitHandler}>
          <div className={styles.inputWrapper}>
            <input
              type="email"
              className={`${styles.inputField} ${
                emailHasError ? styles.inputInvalid : ""
              }`}
              placeholder="Email"
              onChange={emailChangeHandler}
              onBlur={emailBlurHandler}
              value={emailValue}
            />
            {emailHasError && (
              <p className={styles.errorText}>Please enter a valid email.</p>
            )}
          </div>
          <div className={styles.inputWrapper}>
            <input
              type="password"
              className={`${styles.inputField} ${
                passwordHasError ? styles.inputInvalid : ""
              }`}
              placeholder="Password"
              onChange={passwordChangeHandler}
              onBlur={passwordBlurHandler}
              value={passwordValue}
            />
            {passwordHasError && (
              <p className={styles.errorText}>
                Password must contain at least one uppercase letter, at least
                one digit, at least one special character, and be between 8 and
                20 characters long.
              </p>
            )}
          </div>
          <p className={styles.authText}>
            Don't have an account? <Link to="/register">Register</Link>
            </p>
          <button
            className="button"
            disabled={!emailIsValid || !passwordIsValid}
          >
            Submit
          </button>
        </form>
      </div>
    </section>
  );
};

export default Login;
