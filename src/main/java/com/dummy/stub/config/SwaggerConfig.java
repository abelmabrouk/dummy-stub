package com.dummy.stub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Set;

/* 9fbef606107a605d69c0edbcd8029e5d */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(value = "swagger-ui.enabled", havingValue = "true")
public class SwaggerConfig {
    private static final String HEADER_PARAMETER_TYPE = "header";

    @Value("${info.api-version}")
    private String apiVersion;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.stub.onecrf.starterkit.rest"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalOperationParameters(commonHeaders())
                .apiInfo(apiInfo())
                .consumes(Set.of("application/json"))
                .produces(Set.of("application/json"))
                .protocols(Set.of("https"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("stub API")
                .description("Business here")
                .version(apiVersion)
                .build();
    }

    private List<Parameter> commonHeaders() {
        var stringModelRef = new ModelRef("string");

        return List.of(
                new ParameterBuilder()
                        .name("Authorization")
                        .description("This is the authorization JWT Token. It must be provided in the form \"Bearer xxxxxx\"")
                        .modelRef(stringModelRef)
                        .parameterType(HEADER_PARAMETER_TYPE)
                        .required(true)
                        .build(),
                new ParameterBuilder()
                        .name("X-Correlation-ID")
                        .description("Correlation ID between each request from the same scenario")
                        .modelRef(stringModelRef)
                        .parameterType(HEADER_PARAMETER_TYPE)
                        .required(false)
                        .build(),
                new ParameterBuilder()
                        .name("X-Session-ID")
                        .description("Session ID to trace each request from the user")
                        .modelRef(stringModelRef)
                        .parameterType(HEADER_PARAMETER_TYPE)
                        .required(false)
                        .build(),
                new ParameterBuilder()
                        .name("X-Forwarded-For")
                        .description("Forwarded-For helps to make a logs search depending on the user ip")
                        .modelRef(stringModelRef)
                        .parameterType(HEADER_PARAMETER_TYPE)
                        .required(false)
                        .build()
        );
    }
}
