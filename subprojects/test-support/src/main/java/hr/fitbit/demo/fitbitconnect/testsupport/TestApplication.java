package hr.fitbit.demo.fitbitconnect.testsupport;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "hr.fitbit.demo.fitbitconnect")
@EntityScan(basePackages = { "hr.fitbit.demo.fitbitconnect.dao.entity", "hr.fitbit.demo.fitbitconnect.dao.repository" })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ComponentScan(basePackages = {
        "hr.fitbit.demo.fitbitconnect",
        "hr.fitbit.demo.fitbitconnect.apisupport",
        "hr.fitbit.demo.fitbitconnect.dao",
        "hr.fitbit.demo.fitbitconnect.fitbitclient",
        "hr.fitbit.demo.fitbitconnect.users",
        "hr.fitbit.demo.fitbitconnect.security",
        "hr.fitbit.demo.fitbitconnect.testsupport"
})
@TestComponent
public class TestApplication
{
}
