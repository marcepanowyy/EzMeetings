import { Link, Outlet, useLoaderData, useSubmit } from "react-router-dom";
import React from "react";
import { useEffect } from "react";
import { getTokenDuration } from "../utils/auth";
import { decodeToken } from "react-jwt";
import "./RootLayout.css";
import { MyDecodedToken } from "../models/MyDecodedToken.model";
import  useFeedbackReceive  from "../hooks/useFeedbackReceive";
import Feedback from "../ui/Feedback/Feedback";
import { getSimpleToken } from "../utils/auth";
const RootLayout: React.FC = () => {
   
  let token = useLoaderData();
  if (!token) {
    token = getSimpleToken();
  }
  const { feedback,showFeedback } = useFeedbackReceive();
  console.log(feedback)
  let email: string | null = null;
  let myDecodedToken: MyDecodedToken | null;
  if (token && typeof token === "string") {
    myDecodedToken = decodeToken<MyDecodedToken>(token);
    email = myDecodedToken?.sub ?? null;
  }
 
  const submit = useSubmit();
  useEffect(() => {
    if (!token) {
      return;
    }
    if (token === "EXPIRED") {
      submit(null, { action: "/logout", method: "post" });
      return;
    }
    const tokenDuration = getTokenDuration();
    setTimeout(() => {
      submit(null, { action: "/logout", method: "post" });
    }, tokenDuration);

    
  }, [token, submit]);

  const handleLogout = () => {
    submit(null, { action: "/logout", method: "post" });
    //feedback
    showFeedback("success", "Logout successful");
  };

  return (
    <div>
      {token && (
        <div className="user-bar">
          <div className="user-info">
            <span>{email}</span>

            <span className="my-events">
              <Link to="/events">My Events</Link>
            </span>
          </div>
          <div className="btn-logout">
            <button onClick={handleLogout} className="btn-logout">
              Logout
            </button>
          </div>
        </div>
      )}
      {feedback && (
        <Feedback
          feedback={feedback}
          clearFeedback={() => {showFeedback && showFeedback(null, "")}}
        />
      )}
      <Outlet />
    </div>
  );
};

export default RootLayout;
