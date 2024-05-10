package dev.guiga.proj1.user_management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserBlockedException extends ResponseStatusException {
    public UserBlockedException() {
        super(HttpStatus.FORBIDDEN, "User is blocked!");
    }
}
