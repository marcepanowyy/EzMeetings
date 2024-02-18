## Ez Meetings

### Project description:

The project's goal is to build an application similar to Doodle for scheduling events and meetings.
The main functionality will be to allow users to create events with multiple date options.
Invited participants will be able to vote for preferred dates, which will allow to determine the 
final date of the event.


Key features:
* event creation 
* user registration and login
* signing up for events
* voting for preferred dates
* choosing the final date of the event
* event management - editing and deleting (only for the event creator)

### Technology stack:

#### Backend:

<p>

   <a href="https://spring.io/" target="_blank" rel="noreferrer"> 
      <img src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" alt="spring" width="40" height="40"/> 
   </a>

   <a href="https://www.java.com" target="_blank" rel="noreferrer"> 
      <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="40" height="40"/>
   </a>

</p>


#### Frontend:

<p>

   <a href="https://reactjs.org/" target="_blank" rel="noreferrer"> 
      <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/react/react-original-wordmark.svg" alt="react" width="40" height="40"/>
   </a>

   <a href="https://www.typescriptlang.org/" target="_blank" rel="noreferrer"> 
      <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/typescript/typescript-original.svg" alt="typescript" width="40" height="40"/>
   </a>

   <a href="https://www.w3.org/html/" target="_blank" rel="noreferrer"> 
      <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/html5/html5-original-wordmark.svg" alt="html5" width="40" height="40"/> 
   </a>

  <a href="https://www.w3schools.com/css/" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/css3/css3-original-wordmark.svg" alt="css3" width="40" height="40"/>
  </a>

</p>


#### Database and Containerization:

<p>

   <a href="https://www.postgresql.org/" target="_blank" rel="noreferrer"> 
      <img src="https://www.vectorlogo.zone/logos/postgresql/postgresql-icon.svg" alt="postgresql" width="40" height="40"/>
   </a>

   <a href="https://www.docker.com/" target="_blank" rel="noreferrer"> 
      <img src="https://www.vectorlogo.zone/logos/docker/docker-icon.svg" alt="docker" width="40" height="40"/>
   </a>

</p>


### Database Schema:

<img src="assets/db_diagram.png" alt="db_diagram.jpg" height="400px">

### Running the App

Having Docker installed, the application can be run with a single command. Navigate to the project folder (where the docker-compose.yaml file is located) and run the following command:

```
docker-compose up -d 
```

By default, the application will be available on the following ports:
* PostgreSQL database : 5432
* Spring Boot backend : 8080
* React frontend : 3000

### Testing the backend

Import the *backend* folder to Intellij: File -> Open.. Wait for Gradle to download all dependencies and build the project. To test the application's functionality, use the test runner (green arrow next to the test class name).

