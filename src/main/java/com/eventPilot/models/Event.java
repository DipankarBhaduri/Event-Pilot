package com.eventPilot.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "event")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Event extends BaseEntity {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private Reference organizerReference;
    private Boolean recurring;
    private Integer recurrenceCount;
    private LocalDateTime recurringStartingDate;
    private List<Reference> participants;

    public boolean isRecurring() {
        return recurring != null;
    }

    public int getRecurrenceCount() {
        return (recurrenceCount == null) ? 0 : recurrenceCount;
    }
}

/*
scheduling-application
│
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.example.scheduling
│   │   │   │   ├── controller
│   │   │   │   │   ├── EventController.java
│   │   │   │   │   └── UserController.java
│   │   │   │   ├── model
│   │   │   │   │   ├── Event.java
│   │   │   │   │   └── User.java
│   │   │   │   ├── repository
│   │   │   │   │   ├── EventRepository.java
│   │   │   │   │   └── UserRepository.java
│   │   │   │   ├── service
│   │   │   │   │   ├── EventService.java
│   │   │   │   │   └── UserService.java
│   │   │   │   └── SchedulingApplication.java
│   │   ├── resources
│   │   │   └── application.properties
│   └── test
│       ├── java
│       │   └── com.example.scheduling
│       │       └── service
│       │           ├── EventServiceTest.java
│       │           └── UserServiceTest.java
│       └── resources
│           └── application-test.properties
├── target
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
 */