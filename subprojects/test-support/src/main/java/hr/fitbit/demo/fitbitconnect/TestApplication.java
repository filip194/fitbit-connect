package hr.fitbit.demo.fitbitconnect;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "hr.fitbit.demo.fitbitconnect.util",
        "hr.fitbit.demo.fitbitconnect.bean",
        "hr.fitbit.demo.fitbitconnect.aspect",
        "hr.fitbit.demo.fitbitconnect.securityconfig",
        "hr.fitbit.demo.fitbitconnect.service",
        "hr.fitbit.demo.fitbitconnect.controller",
        "hr.fitbit.demo.fitbitconnect.apimodel",
        "hr.fitbit.demo.fitbitconnect.swagger",
        "hr.fitbit.demo.fitbitconnect.pagination",
        "hr.fitbit.demo.fitbitconnect.fixture",
        "hr.fitbit.demo.fitbitconnect.client"

})
public class TestApplication {
}

