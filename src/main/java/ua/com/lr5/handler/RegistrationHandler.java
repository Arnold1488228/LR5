package ua.com.lr5.handler;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ua.com.lr5.entity.User;
import ua.com.lr5.service.UserService;

import org.springframework.stereotype.Component;

@Component
public class RegistrationHandler {

    private final UserService userService;

    public RegistrationHandler(UserService userService) {
        this.userService = userService;
    }


    public Mono<ServerResponse> registrationForm(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("Please provide registration details"));
    }


    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(User.class)
                .flatMap(user -> {
                    if (user.getUsername() == null || user.getPassword() == null) {
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue("Username and password are required"));
                    }

                    return userService.saveUser(user)
                            .flatMap(savedUser -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(BodyInserters.fromValue(savedUser)));
                })
                .onErrorResume(throwable -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue("Invalid data")));
    }
}
