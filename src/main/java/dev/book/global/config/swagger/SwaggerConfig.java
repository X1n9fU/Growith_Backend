package dev.book.global.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${springdoc.servers.local.url}")
    private String localUrl;

    @Value("${springdoc.servers.production.url}")
    private String prodUrl;

    @Bean
    public OpenAPI OpenAPI(){

        Info info = new Info()
                .title("API Documentation")
                .description("Growith 1팀 API 명세서")
                .version("v1.0.0");

        SecurityScheme scheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("access_token");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("cookieAuth");

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes("cookieAuth", scheme))
                .servers(List.of(
                        new Server().url(localUrl).description("Local Server"),
                        new Server().url(prodUrl).description("Production Server"))
                );

    }
}
