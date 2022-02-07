package com.debski.simplefileserver.openapi;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Parameter(description = "File should be of supported type, otherwise exception will be thrown")
public @interface FileBodyParameter {
}
