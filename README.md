Efficient Calendar Event Scheduling Prototype
This project serves as a prototype for a web application designed to streamline the scheduling of calendar events. Developed using Java, Spring Boot, and MongoDB, 
our aim is to provide users with essential functionalities for efficient schedule management. Please adhere to the guidelines below for successful setup and utilization of the application.

Functionalities
User Busy Slots:

Users can effortlessly create busy slots for themselves throughout the day.
Event Retrieval:

Fetch events for individual users and other specified users seamlessly.
Conflict Checking:

Identify events where users have conflicts for a particular day, ensuring a conflict-free schedule.
Optimal Slot Selection:

Organizers can intelligently find the most favorable upcoming empty slot for a given set of users and a specific duration.
Event Creation:

Users can easily create events with other users for a defined start time and end time.
Bonus Extensions
Recurring Events:
Organizers have the flexibility to create recurring events for a specific number of times, maintaining consistent start and end times with the same set of users.
Technology Stack
Java: The primary backend programming language, chosen for its versatility and reliability.
Spring Boot: Selected for its simplicity and efficiency in developing robust applications.
MongoDB: Employed as the NoSQL database to store and manage calendar events.
How to Run the Service
Clone the repository: git clone [repository_url]
Navigate to the project directory: cd calendar-event-scheduler
Build the project: ./mvnw clean install
Run the application: ./mvnw spring-boot:run
The application will initiate, and you can access the APIs to leverage the specified functionalities.

Event Controller
1. Create Busy Schedule Event
Endpoint: /rest/api/createBusySchedule
Method: POST
Request Body: Event object representing a busy schedule
Response: ResponseEntity<Event> - Created event details with HTTP status
2. Retrieve Events by User and Date
Endpoint: /rest/api/getEvent
Method: GET
Request Body: UserDateInfo object containing user and date information
Response: ResponseEntity<List<Event>> - List of events with HTTP status
3. Retrieve Conflicting Events by User and Date
Endpoint: /rest/api/getConflictEvent
Method: GET
Request Body: UserDateInfo object containing user and date information
Response: ResponseEntity<List<Event>> - List of conflicting events with HTTP status
4. Retrieve Upcoming Empty Time Slots
Endpoint: /rest/api/getUpcomingEmptySlot
Method: GET
Request Body: SchedulingRequest object containing scheduling request information
Response: ResponseEntity<List<TimeSlot>> - List of upcoming empty time slots with HTTP status
5. Create Event with Other Users
Endpoint: /rest/api/createEventWithOtherUsers
Method: POST
Request Body: Event object representing an event involving other users
Response: ResponseEntity<Event> - Created event details with HTTP status
User Controller
1. Get All Users
Endpoint: /rest/api/users
Method: GET
Response: ResponseEntity<List<User>> - List of users with HTTP status
2. Get User by ID
Endpoint: /rest/api/{_id}
Method: GET
Response: ResponseEntity<User> - User details with HTTP status
3. Create New User
Endpoint: /rest/api/newUser
Method: POST
Request Body: User object to be created
Response: ResponseEntity<User> - Created user details with HTTP status
4. Update User
Endpoint: /rest/api/updateUser
Method: PUT
Request Body: User object with updated information
Response: ResponseEntity<User> - Updated user details with HTTP status
5. Delete User by ID
Endpoint: /rest/api/{_id}
Method: DELETE
Response: ResponseEntity<DeleteResult> - Deletion result with HTTP status
README Considerations
Reasoning Behind Technical Choices
Java, Spring Boot, and MongoDB were carefully selected for their reliability, ease of use, and suitability for rapid application development.

Trade-offs
Certain trade-offs may exist due to time constraints, potentially leading to limitations in functionality. Future iterations could address these.

Additional Work
Given more time, the focus should be on enhancing API validation, improving error handling, and incorporating advanced features.
