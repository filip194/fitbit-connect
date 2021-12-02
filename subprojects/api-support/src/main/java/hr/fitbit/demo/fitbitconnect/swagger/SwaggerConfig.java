package hr.fitbit.demo.fitbitconnect.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.core.DefaultLinkRelationProvider;
import org.springframework.plugin.core.SimplePluginRegistry;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    public static final String USERS_GROUP_NAME = "users";
    public static final String FITBIT_GROUP_NAME = "fitbit";

    @Value("${app.release.version}")
    private String appReleaseVersion;

    @Value("${app.build.version}")
    private String appBuildVersion;

    @Bean
    public LinkDiscoverers linkDiscoverers() {
        final List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.of(plugins));
    }

    // [Spring Boot 2.6.1 and SpringDoc 1.5.13] Fixes the problem: bean of type LinkRelationProvider is missing
    @Bean
    public LinkRelationProvider linkRelationProvider() {
        return new DefaultLinkRelationProvider();
    }

    /*
    Can be done in application.properties also:
    springdoc.group-configs[0].group=users
    springdoc.group-configs[0].paths-to-match=/api/users/**
    springdoc.group-configs[0].packages-to-scan=hr.fitbit.demo.fitbitconnect.users
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group(USERS_GROUP_NAME)
                .pathsToMatch("/api/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi fitbitApi() {
        return GroupedOpenApi.builder()
                .group(FITBIT_GROUP_NAME)
                .pathsToMatch("/api/fitbit/**")
                .pathsToExclude("/api/fitbit/redirect")
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
//                .addServersItem(new Server().url("http://localhost:9090"))
                .components(new Components()
                        .addSecuritySchemes("basicScheme", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP).scheme("basic")
                        )
                )
                .info(new Info()
                        .title("Fitbit Connect API")
                        .description("Spring Boot 2 multi-module Maven application with connection to Fitbit API over OAuth2")
                        .version(appReleaseVersion + "-" + appBuildVersion)
                        .license(new License().name("MIT License").url("https://mit-license.org/"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Fitbit Connect Developer Documentation")
                        .url(getApiDocsURI())
                );
    }

    private String getApiDocsURI() {
        return "/docs/doc_index.html";
    }

}

