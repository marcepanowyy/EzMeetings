import { useState, useEffect } from "react";
import { decodeToken } from "react-jwt";
import { getSimpleToken } from "../utils/auth";

const useUserDetails = () => {
  const [email, setEmail] = useState(null);
  const token = getSimpleToken();

  useEffect(() => {
    if (token && typeof token === "string") {
      const decodedToken = decodeToken(token);
      setEmail(decodedToken?.sub ?? null);
    }
  }, [token]);

  return email;
};

export default useUserDetails;
