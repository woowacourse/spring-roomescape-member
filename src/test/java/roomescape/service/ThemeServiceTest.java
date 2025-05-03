package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static roomescape.test.fixture.DateFixture.TODAY;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeCreationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;

class ThemeServiceTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final ThemeRepository themeRepository = mock(ThemeRepository.class);
    private final ThemeService themeService = new ThemeService(reservationRepository, themeRepository);

    @DisplayName("모든 테마를 조회할 수 있다")
    @Test
    void canFindAllTheme() {
        List<Theme> expectedThemes = List.of(
                new Theme(1L, "테마1", "설명", "섬네일"),
                new Theme(2L, "테마2", "설명", "섬네일"),
                new Theme(3L, "테마3", "설명", "섬네일"));
        when(themeRepository.findAll()).thenReturn(expectedThemes);

        List<Theme> actualThemes = themeService.findAllTheme();

        assertThat(actualThemes).containsExactlyElementsOf(expectedThemes);
    }

    @DisplayName("ID를 통해 테마를 조회할 수 있다")
    @Test
    void canFindThemeById() {
        Theme expectedTheme = new Theme(1L, "테마1", "설명", "섬네일");
        when(themeRepository.findById(expectedTheme.getId())).thenReturn(Optional.of(expectedTheme));

        Theme actualTheme = themeService.findThemeById(expectedTheme.getId());

        assertThat(actualTheme).isEqualTo(expectedTheme);
    }

    @DisplayName("ID를 통해 테마를 조회할때, 테마가 존재하지 않는다면 예외를 발생시킨다")
    @Test
    void cannotFindThemeById() {
        long noneExistentId = 1L;
        when(themeRepository.findById(noneExistentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.findThemeById(noneExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 테마가 존재하지 않습니다.");
    }

    @DisplayName("기간동안 인기있는 테마를 조회할 수 있다")
    @Test
    void canFindTopThemes() {
        List<Theme> expectedThemes = List.of(
                new Theme(1L, "테마1", "설명", "섬네일"),
                new Theme(2L, "테마2", "설명", "섬네일"),
                new Theme(3L, "테마3", "설명", "섬네일"),
                new Theme(4L, "테마4", "설명", "섬네일"),
                new Theme(5L, "테마5", "설명", "섬네일"),
                new Theme(6L, "테마6", "설명", "섬네일"),
                new Theme(7L, "테마7", "설명", "섬네일"),
                new Theme(8L, "테마8", "설명", "섬네일"),
                new Theme(9L, "테마9", "설명", "섬네일"),
                new Theme(10L, "테마10", "설명", "섬네일"));
        when(themeRepository.getTopThemesByCount(TODAY.minusDays(7), TODAY)).thenReturn(expectedThemes);

        List<Theme> actualThemes = themeService.findTopThemes(TODAY.minusDays(7), TODAY);

        assertThat(actualThemes).containsExactlyElementsOf(expectedThemes);
    }

    @DisplayName("테마를 추가할 수 있다")
    @Test
    void canAddTheme() {
        ThemeCreationRequest request = new ThemeCreationRequest("테마", "설명", "섬네일");
        when(themeRepository.add(Theme.createWithoutId(request.name(), request.thumbnail(), request.thumbnail())))
                .thenReturn(1L);

        long savedId = themeService.addTheme(request);

        assertThat(savedId).isEqualTo(1L);
    }

    @DisplayName("ID를 통해 테마를 삭제할 수 있다")
    @Test
    void canDeleteThemeById() {
        Theme theme = new Theme(1L, "테마", "설명", "섬네일");
        when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));
        when(reservationRepository.checkExistenceInTheme(theme.getId())).thenReturn(false);

        assertAll(
                () -> assertThatCode(() -> themeService.deleteThemeById(theme.getId()))
                        .doesNotThrowAnyException(),
                () -> verify(themeRepository, times(1)).deleteById(theme.getId())
        );
    }

    @DisplayName("삭제할 테마가 존재하지 않는 경우 예외를 발생시킨다")
    @Test
    void cannotDeleteThemeWhenEmptyTheme() {
        long noneExistentId = 1L;
        when(themeRepository.findById(noneExistentId)).thenReturn(Optional.empty());
        when(reservationRepository.checkExistenceInTheme(noneExistentId)).thenReturn(false);

        assertThatThrownBy(() -> themeService.deleteThemeById(noneExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 테마가 존재하지 않습니다.");
    }

    @DisplayName("예약이 존재하는 경우 테마를 삭제할 수 있다")
    @Test
    void cannotDeleteThemeWhenReservationExist() {
        Theme theme = new Theme(1L, "테마", "설명", "섬네일");
        when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));
        when(reservationRepository.checkExistenceInTheme(theme.getId())).thenReturn(true);

        assertThatThrownBy(() -> themeService.deleteThemeById(theme.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 예약이 이미 존재하는 테마를 제거할 수 없습니다.");
    }
}
