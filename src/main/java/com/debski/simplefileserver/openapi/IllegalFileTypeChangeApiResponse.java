package com.debski.simplefileserver.openapi;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "422", description = "Uploaded file had different file type than original file",
        content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
)
public @interface IllegalFileTypeChangeApiResponse {
}
