package com.eventPilot.services;

import com.eventPilot.models.User;
import com.eventPilot.repository.GenericsCollectionHandler;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling user-related operations.
 * This class interacts with the GenericsCollectionHandler to perform CRUD operations on User entities.
 * Author: [Dipankar Bhaduri]
 */
@Service
public class UserService {

    @Autowired
    private GenericsCollectionHandler genericsCollectionHandler;

    /**
     * Retrieve all users from the database.
     * @return ResponseEntity<List<User>> - List of users with HTTP status.
     */
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> userList = genericsCollectionHandler.findAllDocument(User.class);
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieve a user by their unique identifier (_id) from the database.
     * @param _id The unique identifier of the user.
     * @return ResponseEntity<User> - User details with HTTP status.
     */
    public ResponseEntity<User> getUserById(String _id) {
        User user = null;
        try {
            Optional<List<User>> userList = Optional.ofNullable(genericsCollectionHandler.findByField(User.class, "_id", _id));
            if (userList.isPresent() && userList.get().size() > 0) {
                user = userList.get().get(0);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Create or update a user in the database.
     * @param user The user object to be created or updated.
     * @return ResponseEntity<User> - Created or updated user details with HTTP status.
     */
    public ResponseEntity<User> createOrUpdateUser(User user) {
        User responseUser = null;
        try {
            responseUser = genericsCollectionHandler.insertData(user);
            return new ResponseEntity<>(responseUser, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(responseUser, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete a user by their unique identifier (_id) from the database.
     * @param _id The unique identifier of the user to be deleted.
     * @return ResponseEntity<DeleteResult> - Deletion result with HTTP status.
     */
    public ResponseEntity<DeleteResult> deleteUser (String _id) {
        DeleteResult deleteResult = null;
        try {
            deleteResult = genericsCollectionHandler.removeByField(User.class, "_id", _id);
            return new ResponseEntity<>(deleteResult, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(deleteResult, HttpStatus.BAD_REQUEST);
        }
    }
}
