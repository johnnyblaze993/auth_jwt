package com.rimfire.de;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAllUsers();

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsernameAndPassword(String username, String password);

    User saveUser(User user); // Return type should be User

    void deleteUserByEmail(String email);
}
