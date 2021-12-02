package hr.fitbit.demo.fitbitconnect.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${management.allow-all:true}")
    private boolean managementAllowAll;

    @Override
    public void configure(WebSecurity web) {

        if (managementAllowAll) {
            // disable security for management port
            web.ignoring().antMatchers("/actuator/**", "/h2-console/**");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureSessionHandling(http);
        configureSecurityHeaders(http);

        http.authorizeRequests()
                // allow for all
                .antMatchers("/error/**").permitAll()
                .antMatchers("/api/users/register").permitAll()
                .antMatchers("/docs/**").permitAll()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // allow for registered users
                .antMatchers("/").hasRole(UserAuthority.AUTHENTICATED_USER.name())
                .and()
                .httpBasic()
                .realmName("Fitbit Connect")
                .and()
                .logout();

        // disable CSRF
        http.csrf().disable();
    }

    private void configureSessionHandling(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    private void configureSecurityHeaders(HttpSecurity http) throws Exception {
        http.headers()
                .xssProtection().block(true)
                .and()
                // not working with Spring OpenDoc
                // CSP directives to serve content only from origin with an exception for style and fonts which are fetched from whitelisted sites
//                .contentSecurityPolicy("script-src 'self' https://cdnjs.cloudflare.com; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdnjs.cloudflare.com; object-src 'none'; base-uri 'self'; form-action 'self'; default-src 'none'; connect-src 'self'; frame-ancestors 'self'; font-src 'self' data: https://cdnjs.cloudflare.com https://fonts.gstatic.com; img-src 'self' data: https://cdnjs.cloudflare.com")
//                .and()
                // set HSTS header to tell the browser it should only access the webpage and its subdomains using HTTPS for 12 months(31536000 seconds)
                .httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(31536000)
                .and()
                .frameOptions().sameOrigin();
    }

}

