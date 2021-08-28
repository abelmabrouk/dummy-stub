package com.dummy.stub.util.exception;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.dummy.stub.util.exception.suberror.ApiValidationError;
import com.dummy.stub.util.exception.suberror.SubError;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/* 9fbef606107a605d69c0edbcd8029e5d */
@ApiModel(value = "error_response")
public class ErrorResponse {

  @JsonIgnore
  private HttpStatus httpStatus;

  @ApiModelProperty(value = "Error message.")
  private String message;

  private String timestamp;

  @JsonProperty("correlation_id")
  @ApiModelProperty(value = "Correlation ID between each request from the same scenario.", example = "d5a1aa2b-a0e1-43da-8b2c-22f892f19b64", required = true)
  private String correlationId;

  @JsonInclude(NON_EMPTY)
  @JsonProperty("sub_errors")
  @ApiModelProperty(value = "List of sub errors (if any)")
  private List<SubError> subErrors;

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public String getMessage() {
    return message;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public List<SubError> getSubErrors() {
    return subErrors;
  }

  public static final class ErrorResponseBuilder {

    private HttpStatus httpStatus;
    private String message;
    private String correlationId;
    private List<SubError> subErrors = new ArrayList<>();

    private ErrorResponseBuilder() {
    }

    public static ErrorResponseBuilder builder() {
      return new ErrorResponseBuilder();
    }

    public ErrorResponseBuilder withHttpStatus(HttpStatus httpStatus) {
      this.httpStatus = httpStatus;
      return this;
    }

    public ErrorResponseBuilder withMessage(String message) {
      this.message = message;
      return this;
    }

    public ErrorResponseBuilder withCorrelationId(String correlationId) {
      this.correlationId = correlationId;
      return this;
    }

    public ErrorResponseBuilder addSubErrors(List<SubError> subErrors) {
      this.subErrors.addAll(subErrors);
      return this;
    }

    public ErrorResponseBuilder addSubError(SubError subError) {
      this.subErrors.add(subError);
      return this;
    }

    private ErrorResponseBuilder addValidationError(FieldError fieldError) {
      return addValidationError(
          fieldError.getObjectName(),
          fieldError.getField(),
          fieldError.getRejectedValue(),
          fieldError.getDefaultMessage());
    }

    public ErrorResponseBuilder addValidationErrors(List<FieldError> fieldErrors) {
      fieldErrors.forEach(this::addValidationError);
      return this;
    }

    public ErrorResponseBuilder addValidationError(ObjectError objectError) {
      return addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
    }

    private ErrorResponseBuilder addValidationError(String object, String message) {
      return addSubError(ApiValidationError.builder()
          .object(object)
          .message(message)
          .build()
      );
    }

    public ErrorResponseBuilder addValidationError(String object, String field, Object rejectedValue, String message) {
      return addSubError(ApiValidationError.builder()
          .object(object)
          .field(field)
          .message(message)
          .rejectedValue(rejectedValue)
          .build());
    }

    public ErrorResponseBuilder addValidationError(List<ObjectError> globalErrors) {
      globalErrors.forEach(this::addValidationError);
      return this;
    }

    public ErrorResponse build() {
      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.httpStatus = httpStatus;
      errorResponse.message = message;
      errorResponse.subErrors = subErrors;
      errorResponse.timestamp = Instant.now().toString();
      errorResponse.correlationId = correlationId;
      return errorResponse;
    }
  }
}