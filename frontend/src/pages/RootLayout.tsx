import { Link, Outlet, useLoaderData, useSubmit } from "react-router-dom";
import React from "react";
import { useEffect } from "react";
import { getTokenDuration } from "../utils/auth";
import { decodeToken } from "react-jwt";
import "./RootLayout.css";
import { MyDecodedToken } from "../models/MyDecodedToken.model";
const RootLayout: React.FC = () => {
  const token = useLoaderData();

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
      <Outlet />
    </div>
  );
};

export default RootLayout;
