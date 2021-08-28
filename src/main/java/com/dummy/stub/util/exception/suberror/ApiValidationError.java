package com.dummy.stub.util.exception.suberror;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

/* 9fbef606107a605d69c0edbcd8029e5d */
@JsonInclude(NON_NULL)
@Getter
@ApiModel(value = "api_validation_error")
public class ApiValidationError extends SubError {

  private String object;
  private String field;
  @JsonProperty(value = "rejected_value")
  private Object rejectedValue;

  @Builder
  private ApiValidationError(String object, String field, Object rejectedValue, String message) {
    this.object = object;
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }
}
