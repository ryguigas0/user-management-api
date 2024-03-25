package dev.guiga.proj1.user_management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameNotFound extends ResponseStatusException {
    public UsernameNotFound(String username) {
        super(HttpStatus.NOT_FOUND, String.format("Username '%s' not found!", username));
    }
}
