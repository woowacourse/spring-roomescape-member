package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.service.dto.request.ThemeRequest;
import roomescape.service.dto.response.ThemeResponse;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("모든 테마들을 조회한다.")
    void getAllThemes() {
        Theme theme = new Theme(1L, "테마", "테마 설명", "https://example.com");
        BDDMockito.given(themeRepository.findAll())
                .willReturn(List.of(theme));

        List<ThemeResponse> themeResponses = themeService.getAllThemes();

        ThemeResponse expected = ThemeResponse.from(theme);
        assertThat(themeResponses).containsExactly(expected);
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void addTheme() {
        Theme theme = new Theme(1L, "테마", "테마 설명", "https://example.com");
        BDDMockito.given(themeRepository.existsByName(anyString()))
                .willReturn(false);
        BDDMockito.given(themeRepository.save(any()))
                .willReturn(theme);

        ThemeRequest themeRequest = new ThemeRequest("테마", "테마 설명", "https://example.com");
        ThemeResponse themeResponse = themeService.addTheme(themeRequest);

        ThemeResponse expected = new ThemeResponse(1L, "테마", "테마 설명", "https://example.com");
        assertThat(themeResponse).isEqualTo(expected);
    }

    @Test
    @DisplayName("이미 존재하는 이름의 테마를 추가할 수 없다.")
    void addThemeWithDuplicatedName() {
        BDDMockito.given(themeRepository.existsByName(anyString()))
                .willReturn(true);

        ThemeRequest themeRequest = new ThemeRequest("테마", "테마 설명", "https://example.com");

        assertThatThrownBy(() -> themeService.addTheme(themeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 이름의 테마는 이미 존재합니다.");
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteThemeById() {
        BDDMockito.given(themeRepository.existsById(any()))
                .willReturn(true);
        BDDMockito.given(reservationRepository.existsByThemeId(any()))
                .willReturn(false);

        themeService.deleteThemeById(1L);

        BDDMockito.verify(themeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제할 수 없다.")
    void deleteNotExistedTheme() {
        BDDMockito.given(themeRepository.existsById(any()))
                .willReturn(false);

        assertThatThrownBy(() -> themeService.deleteThemeById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 id의 테마가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("사용 중인 테마를 삭제할 수 없다.")
    void deleteThemeInUse() {
        BDDMockito.given(themeRepository.existsById(any()))
                .willReturn(true);
        BDDMockito.given(reservationRepository.existsByThemeId(any()))
                .willReturn(true);

        assertThatThrownBy(() -> themeService.deleteThemeById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마를 사용하는 예약이 존재합니다.");
    }

    @Test
    @DisplayName("인기 있는 테마들을 조회한다.")
    void getPopularThemes() {
        Theme theme = new Theme(1L, "테마", "테마 설명", "https://example.com");
        BDDMockito.given(themeRepository.findPopularThemes(any(), any(), anyInt()))
                .willReturn(List.of(theme));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        int limit = 1;
        List<ThemeResponse> themeResponses = themeService.getPopularThemes(startDate, endDate, limit);

        ThemeResponse expected = ThemeResponse.from(theme);
        assertThat(themeResponses).containsExactly(expected);
    }
}
