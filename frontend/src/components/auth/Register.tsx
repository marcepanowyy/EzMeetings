import React from "react";
import styles from "./AuthForms.module.css";
import { emailValidator, passwordValidator } from "../../utils/validators";

import useInput from "../../utils/use-input"; 
import {Link, useNavigate} from 'react-router-dom';
import { RegisterRequest } from "../../models/api.models";
import { register } from "../../utils/http";
const Register: React.FC = () => {
  const navigate = useNavigate();
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
   
    try {
      await register(registerDetails);
      navigate('/');
      window.location.reload();
    } catch (error) {
      console.error("Register error:", error);
    }
    
    
  };

  return (
    <section className={styles.authSection}>
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
