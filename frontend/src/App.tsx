import { RouterProvider, createBrowserRouter, redirect } from "react-router-dom";
import { tokenLoader, checkAuthLoader } from "./utils/auth.js";
import { action as logoutAction } from "./pages/Logout";

import RootLayout from "./pages/RootLayout";
import Home from "./components/Home";
import EventList from "./components/event/EventList";
import EventDetails from "./components/event/EventDetails";
import CreateEvent from "./pages/CreateEvent";
import EditEvent from "./pages/EditEvent";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";

import { queryClient } from "./utils/http";
import { QueryClientProvider } from "@tanstack/react-query";
function App() {
  const router = createBrowserRouter([
    {
      path: "/",
      element: <RootLayout />,
      loader: tokenLoader,
      children: [
        { index: true, element: <Home /> },
        {
          path: "events",
          children: [
            {
              index: true,
              element: <EventList />,
              loader: checkAuthLoader
            }, 
            {
              path: "new",
              element: <CreateEvent />,
              loader: checkAuthLoader
            },
            {
              path: ":id",

              children: [
                { index: true, element: <EventDetails /> },
                {
                  path: "edit",
                  element: <EditEvent />,
                  loader: checkAuthLoader,
                },
              ],
            },
          ],
        },
        {
          path: "/login",
          element: <Login />,
          loader: () => {
            if (checkAuthLoader() === null) {
              return redirect("/events");
            }
            return null;
          },
        },
        {
          path: "/register",
          element: <Register />,
          loader: () => {
            if (checkAuthLoader() === null) {
              return redirect("/events");
            }
            return null;
          },
        },

        {
          path: "logout",
          action: logoutAction,
          loader: checkAuthLoader,
        },
      ],
    },
  ]);

  return(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />;
    </QueryClientProvider>
    )
}

export default App;
