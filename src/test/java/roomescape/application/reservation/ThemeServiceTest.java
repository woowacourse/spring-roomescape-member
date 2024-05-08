package roomescape.application.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import roomescape.application.ServiceTest;
import roomescape.application.reservation.dto.request.ThemeRequest;
import roomescape.application.reservation.dto.response.ThemeResponse;
import roomescape.application.reservation.ThemeService;
import roomescape.domain.reservation.Theme;
import roomescape.domain.reservation.ThemeRepository;

@ServiceTest
class ThemeServiceTest {
    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("테마를 생성한다.")
    @Test
    void shouldReturnCreatedTheme() {
        ThemeRequest request = new ThemeRequest("테마", "테마 설명", "url");
        themeService.create(request);
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(1);
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void shouldReturnAllThemes() {
        createTheme();
        List<ThemeResponse> themes = themeService.findAll();
        assertThat(themes).hasSize(1);
    }

    @DisplayName("id로 테마를 삭제한다.")
    @Test
    void shouldDeleteThemeWhenDeleteWithId() {
        Theme theme = createTheme();
        themeService.deleteById(theme.getId());
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).isEmpty();
    }

    @DisplayName("예약이 존재하는 테마를 삭제하는 경우, 예외가 발생한다.")
    @Test
    @Sql("/insert-single-reservation.sql")
    void shouldThrowEntityReferenceOnDeleteExceptionWhenDeleteThemeWithReservation() {
        assertThatCode(() -> themeService.deleteById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연관된 예약이 존재하여 삭제할 수 없습니다.");
    }

    private Theme createTheme() {
        return themeRepository.create(new Theme("테마", "테마 설명", "url"));
    }
}
