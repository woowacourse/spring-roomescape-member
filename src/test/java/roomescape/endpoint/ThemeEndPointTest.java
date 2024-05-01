package roomescape.endpoint;

import static org.hamcrest.Matchers.notNullValue;
import static roomescape.endpoint.RequestFixture.themeRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ThemeEndPointTest {

    @DisplayName("테마 목록 조회")
    @Test
    void getThemes() {
        HttpRestTestTemplate.assertGetOk("/themes");
    }

    @DisplayName("테마 추가")
    @Test
    void addTheme() {
        HttpRestTestTemplate.assertPostCreated(themeRequest, "/themes", "id", notNullValue());
    }
}
