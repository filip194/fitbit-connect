package hr.fitbit.demo.fitbitconnect.swagger;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
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
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

    @Bean
    public Docket usersApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(USERS_GROUP_NAME)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api/users(/.*)?$"))
                .build();
    }

    @Bean
    public Docket fitbitApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(FITBIT_GROUP_NAME)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api/fitbit(/.*)?$"))
                .paths(Predicates.not(PathSelectors.regex("/api/fitbit/redirect")))
                .build();
    }

    private ApiInfo getApiInfo() {
        final ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title("Fitbit Connect API");
        apiInfoBuilder.description("Developer documentation can be found here: <a href='" + getApiUserGuideURI() + "'>Fitbit Connect developer guide</a>");
        apiInfoBuilder.version(appReleaseVersion + "-" + appBuildVersion);
        apiInfoBuilder.license("© Free");
        return apiInfoBuilder.build();
    }

    private String getApiUserGuideURI() {
        return "/docs/doc_index.html";
    }

}

