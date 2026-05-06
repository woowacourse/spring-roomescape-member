package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ThemeService themeService;

    @Test
    void 존재하지_않는_예약_삭제_시_에러() {
        Long fakeId = 999L;
        assertThatThrownBy(() -> reservationService.deleteById(fakeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 예약");
    }

    @Test
    void 예약을_저장하고_조회한다() {
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));

        Reservation savedReservation = reservationService.save("맥스", LocalDate.of(2026, 5, 6), savedTime.getId(), savedTheme.getId());
        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations.getFirst().getId()).isEqualTo(savedReservation.getId());
    }

    @Test
    void 예약을_저장하고_삭제한다() {
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Reservation savedReservation = reservationService.save("맥스", LocalDate.of(2026, 5, 6), savedTime.getId(), savedTheme.getId());

        reservationService.deleteById(savedReservation.getId());
        assertThat(reservationService.findAll()).hasSize(0);
    }
}
