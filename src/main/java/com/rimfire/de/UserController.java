package com.rimfire.de;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.server.cors.CrossOrigin;
import jakarta.inject.Inject;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller("/users")
@CrossOrigin(allowedOrigins = "http://localhost:3000")
public class UserController {

    private final UserRepository userRepository;

    @Inject
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<List<User>> getUsers() {
        List<User> users = userRepository.getAllUsers();
        return HttpResponse.ok(users);
    }

    @Get("/login/{username}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<User> loginUser(@PathVariable String username, @PathVariable String password) {
        Optional<User> user = userRepository.getUserByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            return HttpResponse.ok(user.get());
        } else {
            return HttpResponse.notFound();
        }
    }

    @Get("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<User> getUser(@PathVariable String email)

    {
        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isPresent()) {
            return HttpResponse.ok(user.get());
        }
        return HttpResponse.notFound();
    }

    @Post("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<User> addUser(@Body User user) {
        User newUser = userRepository.saveUser(user);
        if (newUser != null) {
            URI location = URI.create("/users/" + newUser.getEmail());
            return HttpResponse
                    .created(newUser)
                    .header("Location", location.toString());
        } else {
            return HttpResponse.serverError(); // Return an error response if user is not saved
        }
    }

    @Delete("/{email}")
    public void deleteUser(@PathVariable String email) throws SQLException {
        userRepository.deleteUserByEmail(email);
    }

}
