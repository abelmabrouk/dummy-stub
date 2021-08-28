package com.dummy.stub.rest.advice;

import com.dummy.stub.util.constant.HeaderConstant;
import com.dummy.stub.util.exception.ErrorResponse;
import com.dummy.stub.util.exception.GatewayException;
import com.dummy.stub.util.exception.suberror.SubError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static java.util.Objects.nonNull;
import static net.logstash.logback.argument.StructuredArguments.value;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * For mor detailed information about how to handle exceptions in "stub format", check the class
 * RestResponseExceptionHandler of CMC error library.
 */
/* 9fbef606107a605d69c0edbcd8029e5d */

@RestControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

  public static final String STATUS_CODE = "status code";
  private static final Logger log = LoggerFactory.getLogger(ErrorHandlerController.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handle5xx(Exception exception, WebRequest request) {
    ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.builder()
        .withMessage(INTERNAL_SERVER_ERROR.getReasonPhrase())
        .withCorrelationId(MDC.get(HeaderConstant.CORRELATION_ID.getValue()))
        .withHttpStatus(INTERNAL_SERVER_ERROR)
        .addSubErrors(
            List.of(
                SubError.SubErrorBuilder.builder()
                    .withMessage(exception.getMessage())
                    .build()
            )
        )
        .build();

    log.error(
        "Unexpected Error while processing request: {}", request.toString(),
        value(STATUS_CODE, INTERNAL_SERVER_ERROR.value()),
        exception
    );
    return createResponseEntity(errorResponse);
  }

  private ResponseEntity<ErrorResponse> getErrorResponse(String message, HttpStatus status, Exception exception) {
    ErrorResponse.ErrorResponseBuilder errorResponseBuilder = ErrorResponse.ErrorResponseBuilder.builder()
        .withMessage(message)
        .withHttpStatus(status)
        .withCorrelationId(MDC.get(HeaderConstant.CORRELATION_ID.getValue()));
    if (exception.getCause() != null) {
      errorResponseBuilder
          .addSubErrors(
              List.of(
                  SubError.SubErrorBuilder.builder()
                      .withMessage(exception.getCause().getMessage())
                      .build()
              )
          );
    }
    ErrorResponse errorResponse = errorResponseBuilder.build();
    return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
  }

  @ExceptionHandler(GatewayException.class)
  public ResponseEntity<Object> handleInfrastructureException(GatewayException exception, WebRequest request) {

    ErrorResponse.ErrorResponseBuilder errorResponseBuilder = ErrorResponse.ErrorResponseBuilder.builder()
        .withMessage(exception.getMessage())
        .withHttpStatus(INTERNAL_SERVER_ERROR)
        .withCorrelationId(MDC.get(HeaderConstant.CORRELATION_ID.getValue()));

    if (nonNull(exception.getCause())) {
      errorResponseBuilder.addSubErrors(
          List.of(
              SubError.SubErrorBuilder.builder()
                  .withMessage(exception.getCause().getMessage())
                  .build()
          )
      );
    }
    log.error(
        "Error while processing request: {}", request.toString(),
        value(STATUS_CODE, INTERNAL_SERVER_ERROR),
        exception
    );

    return createResponseEntity(errorResponseBuilder.build());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    log.error("Bad Request while processing request: {}",
        request.toString(),
        value("status", status),
        exception);

    ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.builder()
        .withCorrelationId(MDC.get(HeaderConstant.CORRELATION_ID.getValue()))
        .withHttpStatus(HttpStatus.BAD_REQUEST)
        .withMessage("Validation error")
        .addValidationErrors(exception.getBindingResult().getFieldErrors())
        .addValidationError(exception.getBindingResult().getGlobalErrors())
        .build();
    return createResponseEntity(errorResponse);
  }

  private ResponseEntity<Object> createResponseEntity(ErrorResponse errorResponse) {
    return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.builder()
        .withMessage("Malformed JSON request")
        .withCorrelationId(MDC.get(HeaderConstant.CORRELATION_ID.getValue()))
        .build();
    return handleExceptionInternal(ex, errorResponse, headers, BAD_REQUEST, request);
  }
}