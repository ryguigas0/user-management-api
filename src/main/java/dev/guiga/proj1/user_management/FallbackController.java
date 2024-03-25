package dev.guiga.proj1.user_management;

import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import dev.guiga.proj1.user_management.exceptions.DuplicateUsernameException;

@RestControllerAdvice
public class FallbackController {

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Object> handleDuplicateUser(DuplicateUsernameException ex, WebRequest req) {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
