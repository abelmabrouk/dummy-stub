package com.dummy.stub.util.exception.suberror;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

/* 9fbef606107a605d69c0edbcd8029e5d */
@JsonInclude(NON_NULL)
@ApiModel(value = "sub_error")
public class SubError {

  protected String message;

  protected SubError() {
  }

  public String getMessage() {
    return message;
  }

  public static final class SubErrorBuilder {

    private String message;

    private SubErrorBuilder() {
    }

    public static SubErrorBuilder builder() {
      return new SubErrorBuilder();
    }

    public SubErrorBuilder withMessage(String message) {
      this.message = message;
      return this;
    }

    public SubError build() {
      SubError subError = new SubError();
      subError.message = this.message;
      return subError;
    }
  }
}

