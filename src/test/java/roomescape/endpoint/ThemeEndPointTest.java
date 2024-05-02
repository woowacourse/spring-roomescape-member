package roomescape.endpoint;

import static org.hamcrest.Matchers.notNullValue;
import static roomescape.endpoint.RequestFixture.themeRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.domain.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ThemeEndPointTest {

    @Autowired
    ThemeRepository themeRepository;

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

    @DisplayName("테마 삭제")
    @Test
    void deleteTheme() {
        HttpRestTestTemplate.assertDeleteNoContent("/themes/1");
    }

    @DisplayName("테마 삭제 불가능 - 해당 테마에 예약 존재")
    @Test
    void deleteThemeFailed() {
        HttpRestTestTemplate.assertDeleteBadRequest("/themes/2");
    }

    @DisplayName("테마 순위 조회")
    @Test
    void getTopThemes() {
        HttpRestTestTemplate.assertGetOk("/themes/rankings");
    }
}
