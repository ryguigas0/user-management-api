package dev.guiga.proj1.user_management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MaxLoginsException extends ResponseStatusException {
    public MaxLoginsException() {
        super(HttpStatus.FORBIDDEN, "Maxed out logins (10)! Please change to a new password!");
    }
}
