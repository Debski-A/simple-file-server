package com.debski.simplefileserver.openapi;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Parameter(description = "Unique id used to identify particular file")
public @interface FileIdParameter {
}
