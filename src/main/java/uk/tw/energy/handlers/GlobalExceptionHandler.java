package uk.tw.energy.handlers;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.tw.energy.domain.ErrorResponse;
import uk.tw.energy.exceptions.RecordNotFoundException;

/**
 * @Author: srinivasun
 * @Since: 04/11/24
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Exception handler for validation errors
     *
     * @param argumentNotValidException takes this argument to collect all the binding result errors
     * @return {code ErrorResponse} error response object with detailed error message and field mapping
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse validationExceptionHandler(MethodArgumentNotValidException argumentNotValidException) {
        log.info("Bad request");
        List<ErrorResponse.ErrorMessage> errorMessages =
                argumentNotValidException.getBindingResult().getFieldErrors().stream()
                        .map(fieldError ->
                                new ErrorResponse.ErrorMessage(fieldError.getField(), fieldError.getDefaultMessage()))
                        .toList();
        return new ErrorResponse(Instant.now(), errorMessages);
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecordNotFoundException.class)
    public ErrorResponse recordNotFoundExceptionHandler(RecordNotFoundException exception) {
        log.info("Record not with ID: {}", exception.getRecordId());

        return new ErrorResponse(Instant.now(), "Record not found with ID: %s".formatted(exception.getRecordId()));
    }
}
