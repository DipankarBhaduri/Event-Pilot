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

@Service
public class UserService {

    @Autowired
    private GenericsCollectionHandler genericsCollectionHandler;

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = genericsCollectionHandler.findAllDocument(User.class);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

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

    public ResponseEntity<User> createOrUpdateUser(User user) {
        User responseUser = null;
        try {
            responseUser = genericsCollectionHandler.insertData(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(responseUser, HttpStatus.OK);
    }

    public ResponseEntity<DeleteResult> deleteUser (String _id) {
        DeleteResult deleteResult = null;
        try {
            deleteResult = genericsCollectionHandler.removeByField(User.class, "_id", _id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(deleteResult, HttpStatus.OK);
    }
}
