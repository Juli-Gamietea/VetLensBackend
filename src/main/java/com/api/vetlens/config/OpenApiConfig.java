package com.api.vetlens.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "VetLens",
                        email = "vetlensapp@gmail.com"
                ),
                description = "Documentación de la API oficial de VetLens",
                title = "VetLens",
                version = "1.0.0"
        ),
        security = {
                @SecurityRequirement(
                        name = "Token"
                )
        }
)

@SecurityScheme(
        name = "Token",
        description = "Ingresar el token de JWT",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in =  SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
