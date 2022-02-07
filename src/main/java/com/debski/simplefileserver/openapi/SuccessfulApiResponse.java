package com.debski.simplefileserver.openapi;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "200", description = "Request successfully processed")
public @interface SuccessfulApiResponse {
}
