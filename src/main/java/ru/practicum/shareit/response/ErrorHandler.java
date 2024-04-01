package ru.practicum.shareit.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.user.UserController;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(assignableTypes = {ItemController.class, UserController.class})
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse onBadRequestException(final BadRequestException exception) {

        log.warn("Exception: {}, Bad request: \n- {}", exception.getClass().getName(), exception.getMessage());

        return ErrorResponse.builder().message(exception.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EmailConflictException.class)
    public ErrorResponse onEmailConflictException(final EmailConflictException exception) {

        log.warn("Exception: {}, Conflict request: \n- {}", exception.getClass().getName(), exception.getMessage());

        return ErrorResponse.builder().message(exception.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidException.class, ConstraintViolationException.class})
    public ErrorResponse onValidateErrorException(final RuntimeException exception) {

        log.warn("Exception: {}, Validation error(s): \n{}", exception.getClass().getName(),
                getExceptionMessage(exception));

        return ErrorResponse.builder().message(exception.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse onNotFoundException(final NotFoundException exception) {

        log.warn("Exception: {}, Error(s): \n{}", exception.getClass().getName(),
                getExceptionMessage(exception));

        return ErrorResponse.builder().message(exception.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({NotImplementedException.class, Exception.class})
    public ErrorResponse onThrowableException(final Exception exception) {

        log.error("Exception: {}", exception.toString());

        return ErrorResponse.builder().message(exception.getMessage()).build();
    }

    private String getExceptionMessage(Throwable exception) {

        return Arrays.stream(exception.getMessage().split("&"))
                .map(message -> "- " + message.trim())
                .collect(Collectors.joining("\n"));
    }
}
