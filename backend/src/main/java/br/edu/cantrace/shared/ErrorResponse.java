package br.edu.cantrace.shared;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    int status,
    String error,
    String message,
    String path,
    LocalDateTime timestamp
) {
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, message, null, LocalDateTime.now());
    }

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, path, LocalDateTime.now());
    }

    public static ErrorResponse of(String message, List<String> errors) {
        return new ErrorResponse(400, "Bad Request", message, null, LocalDateTime.now());
    }

    public static ErrorResponse of(String message) {
        return of(400, "Bad Request", message);
    }
}
