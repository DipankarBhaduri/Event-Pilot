# Efficient Calendar Event Scheduling Prototype 📅

Welcome to the repository hosting our innovative web application prototype, dedicated to simplifying calendar event scheduling. Crafted using Java, Spring Boot, and MongoDB, our goal is to empower users with essential tools for seamless schedule management. Below, you'll discover instructions for deploying the application, an overview of its key features, and considerations for future enhancements.

## Functionalities 🚀

### User Busy Slots
Effortlessly create busy slots throughout the day to streamline your schedule.

### Event Retrieval
Retrieve events for individual users and specified users with ease.

### Conflict Checking
Identify conflicting events for users on a particular day, ensuring a hassle-free schedule.

### Optimal Slot Selection
Intelligently find the most favorable upcoming empty slot for a given set of users and a specific duration.

### Event Creation
Easily create events with other users, specifying start and end times effortlessly.

### Bonus Extensions 🌟

- Recurring Events: Organizers can create recurring events for a set number of times, maintaining consistent start and end times with the same set of users.

## Technology Stack 🛠️

- **Java:** The primary backend programming language, chosen for its versatility and reliability.
- **Spring Boot:** Selected for its simplicity and efficiency in developing robust applications.
- **MongoDB:** Employed as the NoSQL database to store and manage calendar events.

## How to Run the Service ⚙️

1. Clone the repository: `git clone [https://github.com/DipankarBhaduri/Event-Pilot.git]`
2. Navigate to the project directory: `cd calendar-event-scheduler`
3. Build the project: `./mvnw clean install`
4. Run the application: `./mvnw spring-boot:run`

## Event Controller 📝

### 1. Create Busy Schedule Event
Allows users to create busy slots for themselves during the day.

- **Endpoint:** `/rest/api/createBusySchedule`
- **Method:** POST
- **Request Body:** Event object representing a busy schedule
- **Response:** ResponseEntity<Event> - Created event details with HTTP status
![Create Busy Schedule Event](https://github.com/DipankarBhaduri/Event-Pilot/assets/110708060/483ab50b-0106-479e-9b0d-c65fb78999b1)

### 2. Retrieve Events by User and Date
Users can fetch events of themselves and other users.

- **Endpoint:** `/rest/api/getEvent`
- **Method:** GET
- **Request Body:** UserDateInfo object containing user and date information
- **Response:** ResponseEntity<List<Event>> - List of events with HTTP status
![Retrieve Events by User and Date](https://github.com/DipankarBhaduri/Event-Pilot/assets/110708060/57db3f08-afdc-46bc-b26e-c1bf80d7b81d)

### 3. Retrieve Conflicting Events by User and Date
Allows users to fetch events where they have conflicts for themselves for a particular day.

- **Endpoint:** `/rest/api/getConflictEvent`
- **Method:** GET
- **Request Body:** UserDateInfo object containing user and date information
- **Response:** ResponseEntity<List<Event>> - List of conflicting events with HTTP status
![Retrieve Conflicting Events by User and Date](https://github.com/DipankarBhaduri/Event-Pilot/assets/110708060/c42cfe31-3a7a-4f0c-a4fd-76510ade800d)

### 4. Retrieve Upcoming Empty Time Slots
Organizers of an event can fetch the most favorable upcoming empty slot for a given set of users and a particular duration.

- **Endpoint:** `/rest/api/getUpcomingEmptySlot`
- **Method:** GET
- **Request Body:** SchedulingRequest object containing scheduling request information
- **Response:** ResponseEntity<List<TimeSlot>> - List of upcoming empty time slots with HTTP status
![Retrieve Upcoming Empty Time Slots](https://github.com/DipankarBhaduri/Event-Pilot/assets/110708060/f5099b13-f583-4c5c-811a-11b58d226140)

### 5. Create Event with Other Users
Users can create events with other users for a defined start time and end time.

- **Endpoint:** `/rest/api/createEventWithOtherUsers`
- **Method:** POST
- **Request Body:** Event object representing an event involving other users
- **Response:** ResponseEntity<Event> - Created event details with HTTP status
![Create Event with Other Users](https://github.com/DipankarBhaduri/Event-Pilot/assets/110708060/e186ba2e-f8da-462e-befe-90c5976f4832)

## User Controller 👥

### 1. Get All Users
- **Endpoint:** `/rest/api/users`
- **Method:** GET
- **Response:** ResponseEntity<List<User>> - List of users with HTTP status

### 2. Get User by ID
- **Endpoint:** `/rest/api/{_id}`
- **Method:** GET
- **Response:** ResponseEntity<User> - User details with HTTP status

### 3. Create New User
- **Endpoint:** `/rest/api/newUser`
- **Method:** POST
- **Request Body:** User object to be created
- **Response:** ResponseEntity<User> - Created user details with HTTP status

### 4. Update User
- **Endpoint:** `/rest/api/updateUser`
- **Method:** PUT
- **Request Body:** User object with updated information
- **Response:** ResponseEntity<User> - Updated user details with HTTP status

### 5. Delete User by ID
- **Endpoint:** `/rest/api/{_id}`
- **Method:** DELETE
- **Response:** ResponseEntity<DeleteResult> - Deletion result with HTTP status

## README Considerations 📌

### Reasoning Behind Technical Choices 🤔

Java, Spring Boot, and MongoDB were carefully selected for their reliability, ease of use, and suitability for rapid application development.

### Trade-offs 🔄

Certain trade-offs may exist due to time constraints, potentially leading to limitations in functionality. Future iterations could address these.

### Additional Work 🚧

Given more time, the focus should be on enhancing API validation, improving error handling, and incorporating advanced features.

## Author 🖋️

Dipankar Bhaduri
