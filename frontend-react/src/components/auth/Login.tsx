import React from "react";
import styles from "./AuthForms.module.css";
import { emailValidator, passwordValidator } from "../../utils/validators";
import useInput from "../../utils/use-input"; // Adjust the import path as necessary
import {Link} from 'react-router-dom';
const Login: React.FC = () => {
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

  const formSubmitHandler = (event: React.FormEvent) => {
    event.preventDefault();
    if (!emailIsValid || !passwordIsValid) {
      return;
    }
    console.log(emailValue, passwordValue);
    resetEmail();
    resetPassword();
  };

  return (
    <section className={styles.authSection}>
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
            className={styles.authButton}
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
