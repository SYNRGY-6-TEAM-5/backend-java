package com.finalproject.Tiket.Pesawat.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "AeroSwift API", version = "v1"),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecuritySchemes({
        @SecurityScheme(
                name = "Bearer Authentication",
                type = SecuritySchemeType.HTTP,
                bearerFormat = "JWT",
                scheme = "bearer"
        )
})
public class OpenApiConfiguration {
        @Value("${aeroswift.openapi.dev-url}")
        private String devUrl;

        @Bean
        public OpenAPI myOpenAPI() {
                Server devServer = new Server();
                devServer.setUrl(devUrl);
                devServer.setDescription("Server URL in Development environment");

                License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

                io.swagger.v3.oas.models.info.Info info = new io.swagger.v3.oas.models.info.Info()
                        .title("AeroSwift API")
                        .version("1.0")
                        .description("This API exposes endpoints to manage AeroSwift Data.")
                        .license(mitLicense);

                return new OpenAPI().info(info).servers(List.of(devServer));
        }
}
