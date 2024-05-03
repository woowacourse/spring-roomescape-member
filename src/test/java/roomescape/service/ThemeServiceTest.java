package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.theme.CreateThemeRequest;
import roomescape.controller.theme.CreateThemeResponse;
import roomescape.repository.H2ReservationRepository;
import roomescape.repository.H2ThemeRepository;
import roomescape.service.exception.ThemeUsedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@JdbcTest
@Import({ThemeService.class, H2ThemeRepository.class, H2ReservationRepository.class})
class ThemeServiceTest {

    final long LAST_ID = 4;
    final CreateThemeResponse exampleFirstResponse = new CreateThemeResponse(
            1L,
            "Spring",
            "A time of renewal and growth, as nature awakens from its slumber and bursts forth with vibrant colors and fragrant blooms.",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    );

    @Autowired
    ThemeService themeService;

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void getThemes() {
        // given & when
        final List<CreateThemeResponse> themes = themeService.getThemes();

        // then
        assertThat(themes).hasSize((int) LAST_ID);
        assertThat(themes.get(0)).isEqualTo(exampleFirstResponse);
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void addTheme() {
        // given
        final CreateThemeRequest response = new CreateThemeRequest(
                "NewTheme",
                "NewDescription",
                "NewImage"
        );

        // when
        final CreateThemeResponse actual = themeService.addTheme(response);
        final CreateThemeResponse expected = new CreateThemeResponse(
                actual.id(),
                response.name(),
                response.description(),
                response.thumbnail()
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteTheme() {
        // given & when & then
        assertThat(themeService.deleteTheme(LAST_ID)).isOne();
        assertThat(themeService.deleteTheme(LAST_ID)).isZero();
    }

    @Test
    @DisplayName("예약이 있는 테마를 삭제할 경우 예외가 발생한다.")
    void exceptionOnDeletingReserved() {
        assertThatThrownBy(() -> themeService.deleteTheme(2L))
                .isInstanceOf(ThemeUsedException.class);
    }
}
