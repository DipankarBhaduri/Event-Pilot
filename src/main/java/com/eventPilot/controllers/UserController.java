package com.eventPilot.controllers;

import com.eventPilot.models.User;
import com.eventPilot.services.UserService;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller class for handling user-related RESTful API endpoints.
 * Author: [Dipankar Bhaduri]
 */
@RestController
@RequestMapping("/rest/api")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieve all users.
     * @return ResponseEntity<List<User>> - List of users with HTTP status.
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Retrieve a user by their unique identifier (_id).
     * @param _id The unique identifier of the user.
     * @return ResponseEntity<User> - User details with HTTP status.
     */
    @GetMapping("/{_id}")
    public ResponseEntity<User> getUserById(@PathVariable String _id) {
        return userService.getUserById(_id);
    }

    /**
     * Create a new user.
     * @param user The user object to be created.
     * @return ResponseEntity<User> - Created user details with HTTP status.
     */
    @PostMapping("/newUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return userService.createOrUpdateUser(user);
    }

    /**
     * Update an existing user.
     * @param user The user object with updated information.
     * @return ResponseEntity<User> - Updated user details with HTTP status.
     */
    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return userService.createOrUpdateUser(user);
    }

    /**
     * Delete a user by their unique identifier (_id).
     * @param _id The unique identifier of the user to be deleted.
     * @return ResponseEntity<DeleteResult> - Deletion result with HTTP status.
     */
    @DeleteMapping("/{_id}")
    public ResponseEntity<DeleteResult> deleteUser(@PathVariable String _id) {
        return userService.deleteUser(_id);
    }
}