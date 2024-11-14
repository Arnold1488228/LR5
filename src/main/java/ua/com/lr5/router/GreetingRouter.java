package ua.com.lr5.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import ua.com.lr5.handler.GreetingHandler;
import ua.com.lr5.handler.RegistrationHandler;

@Configuration(proxyBeanMethods = false)
public class GreetingRouter {

    @Bean
    public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler, RegistrationHandler registrationHandler) {

        return RouterFunctions
                .route(RequestPredicates.GET("/"), greetingHandler::hello)
                .andRoute(RequestPredicates.GET("/users"), greetingHandler::users)
                .andRoute(RequestPredicates.GET("/admin"), greetingHandler::admin)
                .andRoute(RequestPredicates.GET("/register"), registrationHandler::registrationForm)
                .andRoute(RequestPredicates.POST("/register"), registrationHandler::register);
    }
}
