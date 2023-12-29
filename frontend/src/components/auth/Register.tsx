import React from "react";
import styles from "./AuthForms.module.css";
import { emailValidator, passwordValidator } from "../../utils/validators";

import useInput from "../../hooks/use-input";
import {Link, useNavigate} from 'react-router-dom';
import { RegisterRequest } from "../../models/api.models";
import { register } from "../../utils/http";

import { useFeedback } from '../../hooks/useFeedback';
import Feedback from "../../ui/Feedback/Feedback";
import {useMutation} from '@tanstack/react-query';
import LoadingOverlay from "../../ui/LoadingOverlay/LoadingOverlay";
import axios, {isAxiosError} from "axios";

const Register: React.FC = () => {
  const navigate = useNavigate();
  const { feedback, showFeedback } = useFeedback();
  const {mutate,isPending} = useMutation({
    mutationFn:(registerDetails: RegisterRequest)=> register(registerDetails)
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

  const {
    value: confirmPasswordValue,
    isValid: confirmPasswordIsValid,
    hasError: confirmPasswordHasError,
    valueChangeHandler: confirmPasswordChangeHandler,
    inputBlurHandler: confirmPasswordBlurHandler,
    reset: resetConfirmPassword,
  } = useInput((value: String) => value === passwordValue);

  const formSubmitHandler = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!emailIsValid || !passwordIsValid || !confirmPasswordIsValid) {
      return;
    }
    const registerDetails: RegisterRequest = {
      email: emailValue,
      password: passwordValue,
    };
   
    mutate(registerDetails,{
      onSuccess: (response) => {
          console.log(response);
          navigate('/', { 
            state: { 
              feedbackType: 'success', 
              feedbackMessage: 'Registered successfully',
            }
          });
      },
      onError: (error:Error) => {
          showFeedback('error', error.message  || 'An error occurred');
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
        <h1 className={styles.authTitle}>Register</h1>
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
              <p className={styles.errorText}> Password must contain at least one uppercase letter, at least
              one digit, at least one special character, and be between 8 and
              20 characters long.</p>
            )}
          </div>
          <div className={styles.inputWrapper}>
            <input
              type="password"
              className={`${styles.inputField} ${
                confirmPasswordHasError ? styles.inputInvalid : ""
              }`}
              placeholder="Confirm Password"
              onChange={confirmPasswordChangeHandler}
              onBlur={confirmPasswordBlurHandler}
              value={confirmPasswordValue}
            />
            {confirmPasswordHasError && (
              <p className={styles.errorText}>Passwords must match.</p>
            )}
          </div>
          <p className={styles.authText}>
            Already have an account? <Link to="/login">Login</Link>
            </p>
          <button
            className="button"
            disabled={
              !emailIsValid || !passwordIsValid || !confirmPasswordIsValid
            }
          >
            Submit
          </button>
        </form>
      </div>
    </section>
  );
};

export default Register;
