package roomescape.theme.application;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.infrastructure.ThemeRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class ThemeServiceTest {

    private ThemeRepository themeRepository;
    private ReservationRepository reservationRepository;
    private Clock fixedClock;
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeRepository = mock(ThemeRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        fixedClock = Clock.fixed(
                LocalDate.of(2025, 4, 30).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );

        themeService = new ThemeService(themeRepository, reservationRepository, fixedClock);
    }

    @Test
    @DisplayName("중복되지 않은 테마 추가 시 정상적으로 생성된다")
    void test1() {
        // given
        ThemeRequest request = new ThemeRequest("공포", "아주 무서운 테마", "img.jpg");
        Theme savedTheme = new Theme(1L, "공포", "아주 무서운 테마", "img.jpg");

        given(themeRepository.existsByName("공포")).willReturn(false);
        given(themeRepository.add(any())).willReturn(savedTheme);

        // when
        ThemeResponse response = themeService.add(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("공포");
    }

    @Test
    @DisplayName("이미 존재하는 테마 이름으로 추가하면 예외가 발생한다")
    void test2() {
        // given
        ThemeRequest request = new ThemeRequest("공포", "아주 무서운 테마", "img.jpg");
        given(themeRepository.existsByName("공포")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> themeService.add(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 테마가 존재합니다.");
    }

    @Test
    @DisplayName("전체 테마 목록을 조회하면 모든 테마가 반환된다")
    void test3() {
        // given
        Theme theme1 = new Theme(1L, "공포", "desc1", "img1.jpg");
        Theme theme2 = new Theme(2L, "추리", "desc2", "img2.jpg");

        given(themeRepository.findAll()).willReturn(List.of(theme1, theme2));

        // when
        List<ThemeResponse> result = themeService.findAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name").containsExactly("공포", "추리");
    }

    @Test
    @DisplayName("테마 삭제 시 예약이 존재하면 예외가 발생한다")
    void test4() {
        // given
        given(themeRepository.findById(1L)).willReturn(Optional.of(mock(Theme.class)));
        given(reservationRepository.existsByThemeId(1L)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 예약된 테마의 id는 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하면 예외가 발생한다")
    void test5() {
        // given
        given(themeRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 테마 id가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("예약이 없는 상태로 존재하는 테마는 정상적으로 삭제된다")
    void test6() {
        // given
        given(themeRepository.findById(1L)).willReturn(Optional.of(mock(Theme.class)));
        given(reservationRepository.existsByThemeId(1L)).willReturn(false);

        // when
        themeService.deleteById(1L);

        // then
        then(themeRepository).should().deleteById(1L);
    }

    @Test
    @DisplayName("지난 7일간 가장 많이 예약된 테마 10개를 반환한다")
    void test7() {
        // given
        Theme theme1 = new Theme(1L, "공포", "너무 무서운 테마", "img1.jpg");
        Theme theme2 = new Theme(2L, "추리", "짱 재밌는 테마", "img2.jpg");
        List<Theme> themes = List.of(theme1, theme2);

        given(themeRepository.findTop10MostReservedLastWeek(
                LocalDate.now(fixedClock).minusDays(7),
                LocalDate.now(fixedClock).minusDays(1)))
                .willReturn(themes);

        // when
        List<PopularThemeResponse> result = themeService.findTop10MostReservedLastWeek();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name").containsExactly("공포", "추리");
    }
}
