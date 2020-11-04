package hr.fitbit.demo.fitbitconnect;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@TestComponent
@ComponentScan(basePackages = {
        // api-support
        "hr.fitbit.demo.fitbitconnect.apimodel",
        // "hr.fitbit.demo.fitbitconnect.aspect", // not needed
        "hr.fitbit.demo.fitbitconnect.controller",
        "hr.fitbit.demo.fitbitconnect.pagination",
        "hr.fitbit.demo.fitbitconnect.swagger",
        "hr.fitbit.demo.fitbitconnect.util",

        // fitbit-client
        "hr.fitbit.demo.fitbitconnect.client",

        // test-support
        "hr.fitbit.demo.fitbitconnect.fixture",

        // dao
        "hr.fitbit.demo.fitbitconnect.entity",
        "hr.fitbit.demo.fitbitconnect.repository",

        // security
        "hr.fitbit.demo.fitbitconnect.security",

        // users
        "hr.fitbit.demo.fitbitconnect.service"
})
public class TestApplication {
}
