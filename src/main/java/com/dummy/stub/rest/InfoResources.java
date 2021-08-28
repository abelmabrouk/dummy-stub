package com.dummy.stub.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/* 9fbef606107a605d69c0edbcd8029e5d */
@Api
@Slf4j
@RestController
public class InfoResources {

    @ApiOperation(
            value = "this get the status of the server, it will serve as LivenessProbe & ReadinessProbe",
            tags = "info", response = ServerStatus.class
    )
    @GetMapping("/status")
    public ResponseEntity<ServerStatus> info() {
        log.info("/info endpoint called");
        ServerStatus serverStatus = ServerStatus
                .builder()
                .status("UP")
                .build();
        return ResponseEntity.ok(serverStatus);
    }
}

@Builder
@Getter
@Setter
class ServerStatus {
    private String status;
}
