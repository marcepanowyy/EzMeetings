import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { useQuery, useMutation } from "@tanstack/react-query";
import { getEventDetails, makeVote } from "../utils/http";
import { decodeToken } from "react-jwt";
import { queryClient } from "../utils/http";
import { getSimpleToken } from "../utils/auth";

const useEventDetails = () => {
  const { id } = useParams();
  const token = getSimpleToken();

  const { data, isLoading, isError, error } = useQuery({
    queryKey: ["events", id],
    queryFn: () => getEventDetails(id, token),
  });

  const { mutate, isPending } = useMutation({
    mutationFn: (votes) => makeVote(token, votes, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["events", id] });
    },
  });

  return { data, isLoading, isError, error, mutate, isPending, id, token };
};

export default useEventDetails;
