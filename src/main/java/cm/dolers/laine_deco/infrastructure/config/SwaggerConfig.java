package cm.dolers.laine_deco.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080").description("Serveur local"))
                .addServersItem(new Server().url("https://api.laine-deco.cm").description("Serveur de production"))
                .info(new Info()
                        .title("Laine Déco API")
                        .version("1.0.0")
                        .description("Documentation complète de l'API Laine Déco - Plateforme de vente de laine et accessoires")
                        .contact(new Contact()
                                .name("Support Laine Déco")
                                .email("support@laine-deco.cm")
                                .url("https://laine-deco.cm"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Entrez le token JWT")));
                // Removed global security requirement so docs are accessible without auth
    }
}
