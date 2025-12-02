package project.dorce.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidDatasetStatusException extends RuntimeException {
    public InvalidDatasetStatusException(String message) {
        super(message);
    }
}