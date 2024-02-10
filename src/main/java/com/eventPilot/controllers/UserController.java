package com.eventPilot.controllers;

import com.eventPilot.models.User;
import com.eventPilot.services.UserService;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rest/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{_id}")
    public ResponseEntity<User> getUserById(@PathVariable String _id) {
        return userService.getUserById(_id);
    }

    @PostMapping("/newUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return userService.createOrUpdateUser(user);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return userService.createOrUpdateUser(user);
    }

    @DeleteMapping("/{_id}")
    public ResponseEntity<DeleteResult> deleteUser(@PathVariable String _id) {
        return userService.deleteUser(_id);
    }
}