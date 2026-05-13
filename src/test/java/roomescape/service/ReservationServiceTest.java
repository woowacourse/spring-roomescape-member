package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

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
    void 예약을_저장하고_조회한다() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Reservation savedReservation = reservationService.save("맥스", LocalDate.of(2030, 5, 6), savedTime.getId(),
                savedTheme.getId());

        //when
        List<Reservation> reservations = reservationService.findAll(0, 20);

        //then
        assertThat(reservations.getFirst().getId()).isEqualTo(savedReservation.getId());
    }

    @Test
    void 예약을_저장하고_삭제한다() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Reservation savedReservation = reservationService.save("맥스", LocalDate.of(2030, 5, 6), savedTime.getId(),
                savedTheme.getId());

        //when
        reservationService.deleteById(savedReservation.getId());

        //then
        assertThat(reservationService.findAll(0, 20)).hasSize(0);
    }

    @Test
    void 중복예약_불가() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Reservation savedReservation = reservationService.save("맥스", LocalDate.of(2030, 5, 6), savedTime.getId(),
                savedTheme.getId());

        //when & then
        assertThatThrownBy(() -> reservationService.save(
                "피노",
                LocalDate.of(2030, 5, 6),
                savedTime.getId(),
                savedTheme.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 예약");
    }
}
