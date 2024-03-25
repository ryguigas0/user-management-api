package dev.guiga.proj1.user_management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateUsernameException extends ResponseStatusException {
    public DuplicateUsernameException(String username) {
        super(HttpStatus.NOT_FOUND, String.format("User with username '%s' has already been created!", username));
    }
}
