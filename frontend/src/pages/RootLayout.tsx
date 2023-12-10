import { Outlet,useLoaderData,useSubmit } from "react-router-dom";
import React from "react";
import { useEffect } from "react";
import { getTokenDuration } from "../utils/auth";
import { decodeToken } from "react-jwt";
import './RootLayout.css'; 

const RootLayout: React.FC = () => {
    const token = useLoaderData();
    type MyDecodedToken = {
        sub: string;
        iat: number;
        exp: number;
    };

    let email: string | null = null;
    let myDecodedToken: MyDecodedToken | null;
    if (token && typeof token === 'string') {
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
          {token && email ? (
        <div className="user-bar">
          <span>{email}</span>
          <button onClick={handleLogout}>Logout</button>
        </div>
      ) : null}
      <Outlet />
    </div>
    );
    }


export default RootLayout;