package roomescape.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.config.TestConfig;

@SpringBootTest(classes = TestConfig.class)
@Transactional
public abstract class BaseServiceTest {
}
