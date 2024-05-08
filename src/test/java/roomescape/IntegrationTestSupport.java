package roomescape;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTestSupport {

    protected static final String ADMIN_EMAIL = "admin@admin.com";
    protected static final String ADMIN_PASSWORD = "1234";
    protected static final String ADMIN_NAME = "어드민";
    protected static final String USER_EMAIL = "user1@user.com";
    protected static final String USER_PASSWORD = "1234";
}
