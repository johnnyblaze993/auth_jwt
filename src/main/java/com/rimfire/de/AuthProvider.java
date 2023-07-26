package com.rimfire.de;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;

import jakarta.inject.Singleton;

import java.util.Optional;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Singleton
public class AuthProvider implements AuthenticationProvider<HttpRequest<?>> {

    private final UserRepository userRepository;

    public AuthProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest,
            AuthenticationRequest<?, ?> authenticationRequest) {
        return Flux.create(emitter -> {
            String username = (String) authenticationRequest.getIdentity();
            String password = (String) authenticationRequest.getSecret();

            // Use UserRepository to check credentials
            // Modify the implementation based on your UserRepository
            Optional<User> user = checkCredentials(username, password);

            if (user.isPresent()) {
                // Authentication successful
                emitter.next(AuthenticationResponse.success(username));
            } else {
                // Authentication failed
                emitter.error(AuthenticationResponse.exception());
            }

            emitter.complete();
        });
    }

    private Optional<User> checkCredentials(String username, String password) {
        return userRepository.getUserByUsernameAndPassword(username, password);
    }
}
