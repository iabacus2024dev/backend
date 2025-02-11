package kr.co.iabacus.sales.core.common.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String code;
    private final String status;
    private final String message;
    private final String path;
    private final LocalDateTime timestamp;
    private final Map<String, String> validation = new HashMap<>();

    @Builder
    private ErrorResponse(String code, String status, String message, String path) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message, String path) {
        return ErrorResponse.builder()
            .code(String.valueOf(httpStatus.value()))
            .status(httpStatus.getReasonPhrase())
            .message(message)
            .path(path)
            .build();
    }

    public void addValidation(String fieldName, String errorMessage) {
        validation.put(fieldName, errorMessage);
    }

}
