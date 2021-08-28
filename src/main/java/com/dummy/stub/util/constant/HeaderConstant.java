package com.dummy.stub.util.constant;

import lombok.Getter;

/* 9fbef606107a605d69c0edbcd8029e5d */
@Getter
public enum HeaderConstant {

    CORRELATION_ID("correlation_id"),
    X_CORRELATION_ID("X-Correlation-ID"),
    X_SESSION_ID("X-Session-ID"),
    X_FORWARDED_FOR("X-Forwarded-For");

    private String value;

    HeaderConstant(String value) {
        this.value = value;
    }
}
