package dev.guiga.proj1.user_management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameInvalidPassword extends ResponseStatusException {
    public UsernameInvalidPassword(String username) {
        super(HttpStatus.BAD_REQUEST, String.format("Invalid password for '%s'!", username));
    }
}
