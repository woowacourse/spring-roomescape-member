package roomescape.theme.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.controller.dto.ThemeRankingResponse;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.util.repository.ReservationFakeRepository;
import roomescape.util.repository.ReservationTimeFakeRepository;
import roomescape.util.repository.ThemeFakeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ThemeServiceTest {

    private ThemeService themeService;

    @BeforeEach
    void setup() {
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
        ReservationRepository reservationRepository = new ReservationFakeRepository();
        ThemeRepository themeRepository = new ThemeFakeRepository(reservationRepository);

        List<ReservationTime> times = List.of(
                new ReservationTime(null, LocalTime.of(3, 12)),
                new ReservationTime(null, LocalTime.of(11, 33)),
                new ReservationTime(null, LocalTime.of(16, 54)),
                new ReservationTime(null, LocalTime.of(23, 53))
        );

        for (ReservationTime time : times) {
            reservationTimeRepository.saveAndReturnId(time);
        }

        List<Theme> themes = List.of(
                new Theme(null, "레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new Theme(null, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new Theme(null, "레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        for (Theme theme : themes) {
            themeRepository.saveAndReturnId(theme);
        }

        Member member = new Member(1L, "a", "a", "하루", Role.USER);
        Reservation reservation = new Reservation(null, member, LocalDate.now().minusDays(1),
                reservationTimeRepository.findById(1L).get(), themeRepository.findById(1L).get());

        reservationRepository.saveAndReturnId(reservation);
        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @DisplayName("테마 정보를 추가한다")
    @Test
    void add_theme() {
        // given
        ThemeRequest request = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        ThemeResponse actual = themeService.add(request);

        // then
        ThemeResponse expected = new ThemeResponse(4L, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("테마 정보를 조회한다")
    @Test
    void get_themes() {
        // when
        List<ThemeResponse> actual = themeService.getThemes();

        // then
        assertThat(actual).hasSize(3);
    }

    @DisplayName("테마 데이터를 정상적으로 삭제하면 예외가 발생하지 않는다")
    @Test
    void delete_theme() {
        // given
        Long deleteId = 3L;

        // when & then
        assertThatCode(() -> themeService.remove(deleteId))
                .doesNotThrowAnyException();
    }

    @DisplayName("특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제하면 예외가 발생한다")
    @Test
    void delete_exception() {
        // given
        Long deleteId = 1L;

        // when & then
        assertThatThrownBy(() -> themeService.remove(deleteId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 테마에 대한 예약이 존재합니다.");
    }

    @DisplayName("인기 테마 정보를 조회한다")
    @Test
    void get_theme_rankings_test() {
        // when
        List<ThemeRankingResponse> themeRankings = themeService.getThemeRankings();

        // then
        assertThat(themeRankings).isNotEmpty();
    }

}
