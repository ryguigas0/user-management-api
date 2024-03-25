package dev.guiga.proj1.user_management.exceptions;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super(String.format("User with username '%s' has already been created!", username));
    }
}
