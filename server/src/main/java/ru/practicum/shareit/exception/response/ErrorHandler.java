package ru.practicum.shareit.exception.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotImplementedException;
import ru.practicum.shareit.exception.UnsupportedException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dao.UserDAO;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(assignableTypes = {ItemController.class, UserController.class,
        BookingController.class, ItemRequestController.class, UserDAO.class})
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidException.class, ConstraintViolationException.class})
    public ErrorResponse onValidateErrorException(final RuntimeException exception) {

        log.warn("Exception1: {}, Validation error(s): \n{}", exception.getClass().getName(),
                getExceptionMessage(exception));

        return ErrorResponse.builder()
                .error(exception.getClass().getName())
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UnsupportedException.class})
    public ErrorResponse onUnsupportedException(final RuntimeException exception) {

        log.warn("Exception: {}, Unsupported error(s): \n{}", exception.getClass().getName(),
                getExceptionMessage(exception));

        return ErrorResponse.builder()
                .error(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse onNotFoundException(final NotFoundException exception) {

        log.warn("Exception: {}, Not found: \n{}", exception.getClass().getName(), getExceptionMessage(exception));

        return ErrorResponse.builder()
                .error(exception.getClass().getName())
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse onDataIntegrityViolationException(final DataIntegrityViolationException exception) {

        log.warn("DataIntegrityViolationException: {}, message(s): \n{}", exception.getClass().getName(),
                getExceptionMessage(exception));

        return ErrorResponse.builder()
                .error(exception.getClass().getName())
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({NotImplementedException.class, Exception.class})
    public ErrorResponse onThrowableException(final Exception exception) {

        log.error("Exception: {}, message(s): \n{}", exception.getClass().getName(), getExceptionMessage(exception));

        return ErrorResponse.builder()
                .error(exception.getClass().getName())
                .message(exception.getMessage())
                .build();
    }

    private String getExceptionMessage(Throwable exception) {

        return Arrays.stream(exception.getMessage().split("&"))
                .map(message -> "- " + message.trim())
                .collect(Collectors.joining("\n"));
    }
}
