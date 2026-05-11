package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
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
import roomescape.theme.doamin.Theme;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @DisplayName("예약을 생성한다.")
    @Test
    void createReservationSuccess() {
        // test-data.sql 기준:
        // reservation_time id=1 -> 10:00
        // theme id=1 -> 공포의 저택
        LocalDate reservationDate = LocalDate.now().plusDays(10);

        Reservation savedReservation = reservationService.createReservation(
                "체셔",
                reservationDate,
                1L,
                1L
        );

        assertThat(savedReservation.getId()).isNotNull();

        Reservation foundReservation =
                reservationService.getReservation(savedReservation.getId());

        assertThat(foundReservation.getName()).isEqualTo("체셔");
        assertThat(foundReservation.getDate()).isEqualTo(reservationDate);
        assertThat(foundReservation.getTime().getId()).isEqualTo(1L);
        assertThat(foundReservation.getTheme().getId()).isEqualTo(1L);
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약을 생성하면 예외가 발생한다.")
    @Test
    void createReservationFailByMissingTime() {
        LocalDate reservationDate = LocalDate.now().plusDays(10);

        assertThatThrownBy(() -> reservationService.createReservation(
                "체셔",
                reservationDate,
                999L,
                1L
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @DisplayName("존재하지 않는 테마로 예약을 생성하면 예외가 발생한다.")
    @Test
    void createReservationFailByMissingTheme() {
        LocalDate reservationDate = LocalDate.now().plusDays(10);

        assertThatThrownBy(() -> reservationService.createReservation(
                "체셔",
                reservationDate,
                1L,
                999L
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    @DisplayName("아이디로 예약을 조회할 수 있다.")
    @Test
    void getReservationSuccess() {
        // test-data.sql 기준:
        // 김철수 / CURRENT_DATE + 7 / time_id=3 / theme_id=1
        List<Reservation> reservations =
                reservationService.getReservationsByUsername("김철수");

        Reservation targetReservation = reservations.stream()
                .filter(reservation -> reservation.getDate()
                        .isEqual(LocalDate.now().plusDays(7)))
                .findFirst()
                .orElseThrow();

        Reservation foundReservation =
                reservationService.getReservation(targetReservation.getId());

        assertThat(foundReservation).isEqualTo(targetReservation);
    }

    @DisplayName("존재하지 않는 예약 조회 시 예외가 발생한다.")
    @Test
    void getReservationFailByMissingReservation() {
        assertThatThrownBy(() -> reservationService.getReservation(999999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @DisplayName("사용자 이름으로 예약 목록을 조회한다.")
    @Test
    void getReservationsByUsername() {
        List<Reservation> result =
                reservationService.getReservationsByUsername("김철수");

        // test-data.sql 기준:
        // 과거 2건 + 미래 2건
        assertThat(result).hasSize(4);

        assertThat(result)
                .extracting(Reservation::getName)
                .containsOnly("김철수");
    }
}