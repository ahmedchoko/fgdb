package com.wevioo.fgdb.common.exception;

import com.wevioo.fgdb.common.constants.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class PiExceptionHandler     {
//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//		Map<String, List<String>> body = new HashMap<>();
//
//		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
//				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
//		body.put("errors", errors);
//
//		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//	}



	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<BadRequestResponse> handleUserBadRequestException(BadRequestException ex, HttpServletRequest req) {
		log.error("An exception has bean Thrown "+ex);
		return ExceptionResult.generateBadRequestException(ex.getErrors(), req.getRequestURI(), ex.getFieldName());
	}

	@ExceptionHandler({AlreadyExistException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	ResponseEntity<ConflictResponse> handleUserAlreadyExistException(AlreadyExistException ex, HttpServletRequest req) {
		log.error("An exception has bean Thrown "+ex);
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ConflictResponse(req.getRequestURI(), ex.getCode(), ex.getMessage()));
	}

	@ExceptionHandler(ConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	ResponseEntity<ConflictResponse> handleConflictException(ConflictException ex, HttpServletRequest req) {
		log.error("An exception has bean Thrown "+ex);
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ConflictResponse(req.getRequestURI(), ex.getCode(), ex.getMessage()));
	}
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ResponseEntity<ConflictResponse> handleDataNotFoundException(DataNotFoundException ex, HttpServletRequest req) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ConflictResponse(req.getRequestURI(), ex.getCode(), ex.getMessage()));
	}

//	@ExceptionHandler(AccessDeniedException.class)
//	@ResponseStatus(HttpStatus.UNAUTHORIZED)
//	public ResponseEntity<ConflictResponse> handleUnauthorizedException(AccessDeniedException e,
//			HttpServletRequest request, HttpServletResponse response) {
//		log.error("An exception has bean Thrown "+e);
//		response.setStatus(HttpStatus.UNAUTHORIZED.value());
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ConflictResponse(request.getRequestURI(),
//				e.getLocalizedMessage(), ApplicationConstants.ERROR_USER_NOT_ALLOWED));
//	}

	@ExceptionHandler(HttpServerErrorException.InternalServerError.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ConflictResponse> handleInternalServerErrorException(
			HttpServerErrorException.InternalServerError e, HttpServletRequest request, HttpServletResponse response) {
		log.error("An exception has bean Thrown "+e);
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ConflictResponse(
				request.getRequestURI(), e.getLocalizedMessage(), ApplicationConstants.ERROR_INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ConflictResponse> handleUnauthorizedException(UnauthorizedException e,
			HttpServletRequest request, HttpServletResponse response) {
		log.error("An exception has bean Thrown "+e);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ConflictResponse(request.getRequestURI(),
				ApplicationConstants.ERROR_UNAUTHORIZED_REQUEST, ApplicationConstants.ERROR_UNAUTHORIZED_REQUEST));
	}
	@ExceptionHandler(ForbiddenException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ConflictResponse> handleForbiddenException(ForbiddenException e,
																		HttpServletRequest request, HttpServletResponse response) {
		log.error("An exception has bean Thrown "+e);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ConflictResponse(request.getRequestURI(),
				ApplicationConstants.ERROR_UNAUTHORIZED_REQUEST, ApplicationConstants.ERROR_UNAUTHORIZED_REQUEST));
	}

	

}
