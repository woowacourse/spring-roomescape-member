package roomescape.reservation.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;
import roomescape.reservation.application.service.ReservationService;
import roomescape.reservation.application.service.ReservationTimeService;
import roomescape.reservation.application.service.ThemeService;
import roomescape.reservation.infrastructure.fake.FakeReservationDao;
import roomescape.reservation.infrastructure.fake.FakeReservationTimeDao;
import roomescape.reservation.infrastructure.fake.FakeThemeDao;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ThemeRequest;
import roomescape.reservation.presentation.dto.ThemeResponse;

public class ThemeServiceTest {
    private ThemeService themeService;
    private ReservationService reservationService;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void init() {
        ReservationRepository reservationRepository = new FakeReservationDao();
        ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeDao();
        ThemeRepository themeRepository = new FakeThemeDao(reservationRepository);

        themeService = new ThemeService(reservationRepository, themeRepository);
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
    }

    @Test
    @DisplayName("테마 추가 테스트")
    void createThemeTest() {
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        // when
        ThemeResponse theme = themeService.createTheme(themeRequest);

        // then
        assertThat(theme.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("테마 전체 조회 테스트")
    void getThemesTest() {
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        themeService.createTheme(themeRequest);

        // when
        List<ThemeResponse> themes = themeService.getThemes();

        // then
        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("테마 삭제 테스트")
    void deleteThemeTest() {
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        themeService.createTheme(themeRequest);

        // when
        themeService.deleteTheme(1L);

        // then
        assertThat(themeService.getThemes()).hasSize(0);
    }

    @Test
    @DisplayName("저장되어 있지 않은 id로 요청을 보내면 예외가 발생한다.")
    void deleteExceptionTest() {
        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 삭제되어 있는 리소스입니다.");
    }

    @Test
    @DisplayName("인기 테마 조회 테스트")
    void getPopularThemesTest() {
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        themeService.createTheme(themeRequest);

        ThemeRequest themeRequest2 = new ThemeRequest(
                "레벨3 탈출",
                "우테코 레벨3를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        themeService.createTheme(themeRequest2);

        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeService.createReservationTime(reservationTimeRequest);

        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.of(2025, 8, 5),
                "브라운",
                2L,
                1L
        );
        reservationService.createReservation(reservationRequest);

        // when
        final List<ThemeResponse> popularThemes = themeService.getPopularThemes();

        // then
        assertThat(popularThemes.stream()
                .map(ThemeResponse::getId)
                .toList()
        ).isEqualTo(List.of(2L, 1L));
    }
}
