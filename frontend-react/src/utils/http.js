import { QueryClient } from "@tanstack/react-query";
import { redirect, json } from "react-router-dom";
import { getAuthToken } from "./auth";

export const queryClient = new QueryClient();