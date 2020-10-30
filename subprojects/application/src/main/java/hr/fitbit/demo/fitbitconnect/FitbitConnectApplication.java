package hr.fitbit.demo.fitbitconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "hr.fitbit.demo.fitbitconnect.util",
        "hr.fitbit.demo.fitbitconnect.aspect",
        "hr.fitbit.demo.fitbitconnect.security",
        "hr.fitbit.demo.fitbitconnect.service",
        "hr.fitbit.demo.fitbitconnect.controller",
        "hr.fitbit.demo.fitbitconnect.apimodel.fitbit",
        "hr.fitbit.demo.fitbitconnect.apimodel.error",
        "hr.fitbit.demo.fitbitconnect.apimodel.user",
        "hr.fitbit.demo.fitbitconnect.swagger",
        "hr.fitbit.demo.fitbitconnect.pagination",
        "hr.fitbit.demo.fitbitconnect.client"
})
@EntityScan("hr.fitbit.demo.fitbitconnect.entity")
@EnableJpaRepositories("hr.fitbit.demo.fitbitconnect.repository")
public class FitbitConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitbitConnectApplication.class, args);
    }

}
