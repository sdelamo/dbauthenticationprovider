package example.micronaut.controllers;

import example.micronaut.security.Roles;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.annotation.security.RolesAllowed;

@Controller("/api")
public class ApiController {
    @RolesAllowed(Roles.ROLE_USER)
    @Get("/hello")
    Message index() {
        return new Message("Hello World");
    }
}
