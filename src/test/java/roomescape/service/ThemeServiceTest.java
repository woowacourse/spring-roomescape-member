package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialDataFixture.INITIAL_THEME_COUNT;
import static roomescape.InitialDataFixture.NOT_RESERVATION_THEME;
import static roomescape.InitialDataFixture.THEME_1;
import static roomescape.InitialDataFixture.THEME_2;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.exceptions.ClientException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ThemeServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("중복된 테마를 저장하려고 하면 예외가 발생한다.")
    void saveDuplicatedTheme() {
        ThemeAddRequest themeAddRequest = new ThemeAddRequest(
                THEME_1.getName().getName(),
                THEME_1.getDescription(),
                THEME_1.getThumbnail()
        );

        assertThatThrownBy(() -> themeService.addTheme(themeAddRequest))
                .isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("테마를 저장하면 id를 자동으로 생성한다.")
    void saveTheme() {
        ThemeAddRequest themeAddRequest = new ThemeAddRequest(
                "새로운 테마 이름",
                THEME_1.getDescription(),
                THEME_1.getThumbnail()
        );

        ThemeResponse themeResponse = themeService.addTheme(themeAddRequest);

        assertThat(themeResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("저장된 모든 테마를 반환한다.")
    void findThemes() {
        List<ThemeResponse> themes = themeService.findThemes();

        assertThat(themes.size()).isEqualTo(INITIAL_THEME_COUNT);
    }

    @Test
    @DisplayName("인기순으로 지정된 갯수만큼의 테마를 반환한다.")
    void findTrendingThemes() {
        List<ThemeResponse> themes = themeService.findTrendingThemes(1L);

        assertThat(themes).containsExactly(new ThemeResponse(THEME_1));
    }

    @Test
    @DisplayName("id값으로 테마를 조회한다.")
    void getTheme() {
        ThemeResponse themeResponse = themeService.getTheme(THEME_2.getId());

        assertThat(themeResponse).isEqualTo(new ThemeResponse(THEME_2));
    }

    @Test
    @DisplayName("id값에 맞는 테마를 삭제한다.")
    void deleteTheme() {
        themeService.deleteTheme(NOT_RESERVATION_THEME.getId());

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);

        assertThat(count).isEqualTo(INITIAL_THEME_COUNT - 1);
    }
}
