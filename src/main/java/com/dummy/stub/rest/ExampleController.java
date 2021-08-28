package com.dummy.stub.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.logstash.logback.argument.StructuredArguments.value;

@Slf4j
@RestController
@RequestMapping("/v1")
public class ExampleController {

    @GetMapping("/hello/{name}")
    public ResponseEntity sayHello(@PathVariable String name) {
        // here value is used to add name parameter in logs with it's value
        log.info("call hello endpoint", value("name", name));
        String result = "Hello " + name;
        return ResponseEntity.ok(result);
    }

}
