import {
  RouterProvider,
  createBrowserRouter,
  Navigate,
} from "react-router-dom";
import { tokenLoader, checkAuthLoader } from "./utils/auth.js";
import { action as logoutAction } from "./pages/Logout";

import RootLayout from "./pages/RootLayout";
import Home from "./components/Home";
import EventList from "./components/event/EventList";
import EventDetails from "./components/event/EventDetails";
import CreateEvent from "./components/event/CreateEvent";
import EditEvent from "./components/event/EditEvent";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";

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
              // loader: checkAuthLoader
            }, //bedzie zawieral info o eventach uzytkownika zalogowanego
            {
              path: "new",
              element: <CreateEvent />,
              // loader: checkAuthLoader
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
        { path: "/login", element: <Login /> },
        { path: "/register", element: <Register /> },

        {
          path: "logout",
          action: logoutAction,
          loader: checkAuthLoader,
        },
      ],
    },
  ]);

  return <RouterProvider router={router} />;
}

export default App;
