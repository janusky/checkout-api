package checkoutapi.confg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "User API", version = "${api.version}",
        contact = @Contact(name = "Manuel", email = "janusky@gmail.com", url = "https://github.com/janusky"),
        // license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), termsOfService = "${app.term.uri}",
        description = "${springdoc.description}"),
    servers = {
            @Server(url = "http://${server.address}:${server.port}", description = "Development"),
            @Server(url = "https://${server.address}:${server.port}", description = "Production")})
public class OpenAPIConfig {

    /**
     * Configure the OpenAPI components.
     *
     * @return Returns fully configure OpenAPI object
     * @see OpenAPI
     */
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .description("Provide the JWT token. JWT token can be obtained from the Login API.")
                                .bearerFormat("JWT")));
    }
}
