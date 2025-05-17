package roomescape.presentation;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import roomescape.TestRepositoryConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@ActiveProfiles("test")
@Import(TestRepositoryConfig.class)
public abstract class AbstractControllerTest {
}
