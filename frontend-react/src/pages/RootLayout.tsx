import { Outlet,useLoaderData,useSubmit } from "react-router-dom";
import React from "react";
import { useEffect } from "react";
import { getTokenDuration } from "../utils/auth";


const RootLayout: React.FC = () => {
    
    const token = useLoaderData();
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
    
    
    return (
        <div>
            <h1>RootLayout</h1>
            <Outlet />
        </div>
    );
    }


export default RootLayout;