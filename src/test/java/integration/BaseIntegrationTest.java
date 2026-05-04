package integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import roomescape.RoomescapeApplication;

@SpringBootTest(classes = {
        RoomescapeApplication.class,
        BaseIntegrationTest.class,
})
@ActiveProfiles("test")
@ComponentScan(basePackages = "integration")
public class BaseIntegrationTest {
}
