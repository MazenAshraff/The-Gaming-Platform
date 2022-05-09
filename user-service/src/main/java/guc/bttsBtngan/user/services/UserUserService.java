package guc.bttsBtngan.user.services;

import guc.bttsBtngan.user.data.UserUserInteraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service // to specify that this class is responsible for the business logic
public class UserUserService {
    // this class will deal with all user-user interaction and database operations in postgres

    private final UserRepository userRepository;
    // the repository for the user table in postgres to deal with database operations including CRUD operations

    @Autowired
    public UserUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAllUsers() {
        return userRepository.findAll().toString();
    }

    public String getUser(Long id) {
        Optional<UserUserInteraction> userId = userRepository.findById(id);
        return userId.toString();
    }

    public void registerUser(UserUserInteraction user) {
        // register a user
        Optional<UserUserInteraction> email = userRepository.findByEmail2(user.getEmail());
        // check if the email is already registered
        if (email.isPresent()) {
            // if the user email already exists
            throw new IllegalStateException("Email already exists");
        }
        // TODO: password hashing
        // TODO: email verification
        // if the email is not registered yet then save the user
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        // delete a user
        boolean exists = userRepository.existsById(id);
        // check if the user exists
        if (!exists) {
            // if the user does not exist
            throw new IllegalStateException("User does not exist");
        }
        // if the user exists then delete the user
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(Long id, String username, String email, String password) {
        // update a user
        UserUserInteraction user = userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User does not exist"));

        // if the name not equal to null, not empty & not the same as the current name
        if(username != null && username.length() > 0 && !Objects.equals(username, user.getUserName())) {
            // update the name
            user.setUserName(username);
        }
        // if email is not null, not empty & not the same as the current email & not already have been taken
        if(email != null && email.length() > 0 && !Objects.equals(email, user.getEmail())){
            Optional<UserUserInteraction> emailExists = userRepository.findByEmail2(email);
            // check if the email is already registered
            if (emailExists.isPresent()) {
                // if the user email already exists
                throw new IllegalStateException("Email already exists");
            } else {
                // update the email
                user.setEmail(email);
            }
        }
        // if password is not null, not empty & not the same as the current password
        if(password != null && password.length() > 0 && !Objects.equals(password, user.getPassword())){
            // update the password
            user.setPassword(password);
        }
    }

    public String getAllphotoRef(String photoRef) {
        Optional<UserUserInteraction> user = userRepository.findByphotoRef(photoRef);
        return userRepository.findByphotoRef(photoRef).toString();
//        System.out.println(userRepository.findByphotoRef(photoRef).toString());
//        return "hello";
    }


}
