package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialDataFixture.THEME_1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.exceptions.UserException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("중복된 테마를 저장하려고 하면 예외가 발생한다.")
    void saveDuplicatedTime() {
        ThemeAddRequest themeAddRequest = new ThemeAddRequest(
                THEME_1.getName().getName(),
                THEME_1.getDescription(),
                THEME_1.getThumbnail()
        );

        assertThatThrownBy(() -> themeService.addTheme(themeAddRequest))
                .isInstanceOf(UserException.class);
    }

}
