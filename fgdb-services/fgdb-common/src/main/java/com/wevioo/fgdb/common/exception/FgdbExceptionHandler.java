package com.wevioo.fgdb.common.exception;

import com.wevioo.fgdb.common.constants.ApplicationConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;


@RestControllerAdvice
@Slf4j
public class FgdbExceptionHandler {

    /**
     * Handles BadRequestException and returns a 400 BAD_REQUEST response.
     * Logs the error and generates a detailed response using ExceptionResult utility.
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BadRequestResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest req) {
        log.error("BadRequestException thrown: {}", ex);
        return ExceptionResult.generateBadRequestException(ex.getErrors(), req.getRequestURI(), ex.getFieldName());
    }

    /**
     * Handles AlreadyExistException and returns a 409 CONFLICT response.
     * Logs the error and returns a ConflictResponse.
     */
    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ConflictResponse> handleAlreadyExistException(AlreadyExistException ex, HttpServletRequest req) {
        log.error("AlreadyExistException thrown: {}", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ConflictResponse(req.getRequestURI(), ex.getCode(), ex.getMessage()));
    }

    /**
     * Handles ConflictException and returns a 409 CONFLICT response.
     * Logs the error and returns a ConflictResponse.
     */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ConflictResponse> handleConflictException(ConflictException ex, HttpServletRequest req) {
        log.error("ConflictException thrown: {}", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ConflictResponse(req.getRequestURI(), ex.getCode(), ex.getMessage()));
    }

    /**
     * Handles DataNotFoundException and returns a 404 NOT_FOUND response.
     * Logs the error and returns a ConflictResponse.
     */
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ConflictResponse> handleDataNotFoundException(DataNotFoundException ex, HttpServletRequest req) {
        log.error("DataNotFoundException thrown: {}", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ConflictResponse(req.getRequestURI(), ex.getCode(), ex.getMessage()));
    }

    /**
     * Handles InternalServerErrorException and returns a 500 INTERNAL_SERVER_ERROR response.
     * Logs the error and returns a ConflictResponse.
     */
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ConflictResponse> handleInternalServerErrorException(
            HttpServerErrorException.InternalServerError ex, HttpServletRequest req) {
        log.error("InternalServerErrorException thrown: {}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ConflictResponse(req.getRequestURI(), ex.getMessage(), ApplicationConstants.ERROR_INTERNAL_SERVER_ERROR));
    }

    /**
     * Handles UnauthorizedException and returns a 401 UNAUTHORIZED response.
     * Logs the error and returns a ConflictResponse.
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ConflictResponse> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest req) {
        log.error("UnauthorizedException thrown: {}", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ConflictResponse(req.getRequestURI(), ApplicationConstants.ERROR_UNAUTHORIZED_REQUEST, ex.getMessage()));
    }

    /**
     * Handles ForbiddenException and returns a 403 FORBIDDEN response.
     * Logs the error and returns a ConflictResponse.
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ConflictResponse> handleForbiddenException(ForbiddenException ex, HttpServletRequest req) {
        log.error("ForbiddenException thrown: {}", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ConflictResponse(req.getRequestURI(), ApplicationConstants.ERROR_FORBIDDEN_REQUEST, ex.getMessage()));
    }
}