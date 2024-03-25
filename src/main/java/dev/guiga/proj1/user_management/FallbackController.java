package dev.guiga.proj1.user_management;

import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class FallbackController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleDuplicateUser(ResponseStatusException ex, WebRequest req) {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("message", ex.getReason());

        return new ResponseEntity<>(body, ex.getStatusCode());
    }
}
