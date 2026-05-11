package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.doamin.Theme;
import roomescape.theme.service.dto.ThemeTimeAvailability;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("인기 테마를 조회한다.")
    @Test
    void getPopularThemes() {
        List<Theme> result = themeService.getPopularThemes(3);

        assertThat(result).hasSize(3);

        // test-data.sql 기준 인기순
        assertThat(result.get(0).getName()).isEqualTo("공포의 저택");
        assertThat(result.get(1).getName()).isEqualTo("탐정 사무소");
        assertThat(result.get(2).getName()).isEqualTo("마법사의 연구실");
    }

    @DisplayName("전체 테마를 조회할 수 있다.")
    @Test
    void getAllThemes() {
        List<Theme> result = themeService.getAllThemes();

        assertThat(result).hasSize(4);
        assertThat(result)
                .extracting(Theme::getName)
                .containsExactlyInAnyOrder(
                        "공포의 저택",
                        "우주 정거장",
                        "마법사의 연구실",
                        "탐정 사무소"
                );
    }

    @DisplayName("테마의 날짜별 예약 가능 시간을 조회할 수 있다.")
    @Test
    void getThemeTimeAvailability() {
        // test-data.sql 기준:
        // 공포의 저택(theme 1) / CURRENT_DATE + 7 / time_id 3(12:00) 예약 존재
        LocalDate date = LocalDate.now().plusDays(7);

        List<ThemeTimeAvailability> result =
                themeService.getThemeTimeAvailability(1L, date);

        assertThat(result).hasSize(13);

        ThemeTimeAvailability reservedTime = result.stream()
                .filter(it -> it.reservationTime().getId().equals(3L))
                .findFirst()
                .orElseThrow();

        ThemeTimeAvailability availableTime = result.stream()
                .filter(it -> it.reservationTime().getId().equals(1L))
                .findFirst()
                .orElseThrow();

        assertThat(reservedTime.reservationTime().getStartAt())
                .isEqualTo(LocalTime.of(12, 0));
        assertThat(reservedTime.isAvailable()).isFalse();

        assertThat(availableTime.reservationTime().getStartAt())
                .isEqualTo(LocalTime.of(10, 0));
        assertThat(availableTime.isAvailable()).isTrue();
    }

    @DisplayName("특정 사용자의 예약 내역이 정상적으로 조회된다.")
    @Test
    void reservationDataExists() {
        List<Reservation> reservations = reservationRepository.findAllByName("김철수");

        // test-data.sql 기준:
        // 과거 2건 (CURRENT_DATE -7, -5)
        // 미래 2건 (CURRENT_DATE +4, +7)
        assertThat(reservations).hasSize(4);
    }
}