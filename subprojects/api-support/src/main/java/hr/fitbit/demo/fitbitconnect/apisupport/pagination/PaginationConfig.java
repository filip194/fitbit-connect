package hr.fitbit.demo.fitbitconnect.apisupport.pagination;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@EnableConfigurationProperties(SpringDataWebProperties.class)
@EnableSpringDataWebSupport
public class PaginationConfig {

    @Bean
    @ConditionalOnMissingBean
    public PageableHandlerMethodArgumentResolverCustomizer pageableHandler(SpringDataWebProperties properties) {
        return new SpringDataWebAutoConfiguration(properties).pageableCustomizer();
    }
}
