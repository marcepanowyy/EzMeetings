import { useQuery } from "@tanstack/react-query";
import { getEvents } from "../utils/http";
import { getSimpleToken, getEmailFromToken } from "../utils/auth";

const useEventsData = () => {
  const token = getSimpleToken();
  const email = getEmailFromToken(token);

  const { data, isLoading, isError, error } = useQuery({
    queryKey: ["events"],
    queryFn: () => getEvents(token),
    staleTime: 5000,
  });

  return { data, isLoading, isError, error, email };
};

export default useEventsData;
