package roomescape.reservation.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.controller.dto.ThemeRankingResponse;
import roomescape.reservation.controller.dto.ThemeRequest;
import roomescape.reservation.controller.dto.ThemeResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.repository.ThemeRepository;
import roomescape.util.repository.ReservationFakeRepository;
import roomescape.util.repository.ReservationTimeFakeRepository;
import roomescape.util.repository.ThemeFakeRepository;

class ThemeServiceTest {

    private ThemeService themeService;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setup() {
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
        ReservationRepository reservationRepository = new ReservationFakeRepository();
        themeRepository = new ThemeFakeRepository(reservationRepository);
        Clock clock = Clock.fixed(Instant.parse("2025-04-03T23:59:59Z"), ZoneOffset.UTC);

        List<ReservationTime> times = List.of(
                new ReservationTime(null, LocalTime.of(3, 12)),
                new ReservationTime(null, LocalTime.of(11, 33)),
                new ReservationTime(null, LocalTime.of(16, 54)),
                new ReservationTime(null, LocalTime.of(23, 53))
        );

        List<Theme> themes = List.of(
                new Theme(null, "레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new Theme(null, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new Theme(null, "레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new Theme(null, "레벨4 탈출", "우테코 레벨4를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        for (ReservationTime time : times) {
            reservationTimeRepository.saveAndReturnId(time);
        }

        for (Theme theme : themes) {
            themeRepository.saveAndReturnId(theme);
        }

        List<Reservation> reservations = List.of(
                new Reservation(null, "루키", LocalDate.of(2025, 3, 28), reservationTimeRepository.findById(1L).get(),
                        themeRepository.findById(1L).get()),
                new Reservation(null, "슬링키", LocalDate.of(2025, 4, 2), reservationTimeRepository.findById(2L).get(),
                        themeRepository.findById(2L).get()),
                new Reservation(null, "범블비", LocalDate.of(2025, 5, 15), reservationTimeRepository.findById(3L).get(),
                        themeRepository.findById(3L).get())
        );

        for (Reservation reservation : reservations) {
            reservationRepository.saveAndReturnId(reservation);
        }

        themeService = new ThemeService(themeRepository, reservationRepository, clock);
    }

    @DisplayName("테마 정보를 추가한다")
    @Test
    void add_theme() {
        // given
        ThemeRequest request = new ThemeRequest("레벨5 탈출", "우테코 레벨5를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        ThemeResponse response = themeService.add(request);

        // then
        Theme savedTheme = themeRepository.findById(5L).get();
        assertAll(
                () -> assertThat(response.id()).isEqualTo(savedTheme.getId()),
                () -> assertThat(response.name()).isEqualTo(savedTheme.getName()),
                () -> assertThat(response.description()).isEqualTo(savedTheme.getDescription()),
                () -> assertThat(response.thumbnail()).isEqualTo(savedTheme.getThumbnail())
        );
    }

    @DisplayName("테마 정보를 조회한다")
    @Test
    void get_themes() {
        // when
        List<ThemeResponse> themes = themeService.getThemes();

        // then
        assertAll(
                () -> assertThat(themes).hasSize(4),
                () -> assertThat(themes).extracting(ThemeResponse::name)
                        .containsExactlyInAnyOrder("레벨1 탈출", "레벨2 탈출", "레벨3 탈출", "레벨4 탈출"),
                () -> assertThat(themes).extracting(ThemeResponse::id)
                        .containsExactlyInAnyOrder(1L, 2L, 3L, 4L)
        );
    }

    @DisplayName("테마 정보를 삭제한다")
    @Test
    void delete_theme() {
        // given
        Long removeId = 4L;

        // when
        themeRepository.deleteById(removeId);

        // then
        assertAll(
                () -> assertThat(themeRepository.findAll()).hasSize(3),
                () -> assertThat(themeRepository.findById(removeId).isEmpty()).isTrue()
        );

    }

    @DisplayName("특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제하면 예외가 발생한다")
    @Test
    void delete_exception() {
        // given
        Long deleteId = 1L;

        // when & then
        assertThatThrownBy(() -> themeService.remove(deleteId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 테마와 연관된 예약이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("인기 테마 정보를 조회한다")
    @Test
    void get_theme_rankings_test() {
        // when
        List<ThemeRankingResponse> actual = themeService.getThemeRankings();

        // then
        List<ThemeRankingResponse> expected = List.of(
                new ThemeRankingResponse("레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new ThemeRankingResponse("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );
        assertThat(actual).containsExactlyElementsOf(expected);
    }

}
