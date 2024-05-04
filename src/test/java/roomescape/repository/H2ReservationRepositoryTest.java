package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import roomescape.BasicAcceptanceTest;
import roomescape.TestFixtures;
import roomescape.domain.Reservation;

@Sql("/setReservation.sql")
class H2ReservationRepositoryTest extends BasicAcceptanceTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("DB에 저장된 전체 예약을 반환한다")
    @Test
    void findAll() {
        List<Reservation> reservations = List.of(
                TestFixtures.RESERVATION_1, TestFixtures.RESERVATION_2, TestFixtures.RESERVATION_3,
                TestFixtures.RESERVATION_4
        );

        assertThat(reservationRepository.findAll()).isEqualTo(reservations);
    }

    @DisplayName("예약을 DB에 저장한다")
    @Test
    void save() {
        reservationRepository.save(TestFixtures.RESERVATION_5);

        List<Reservation> expectedReservations = List.of(
                TestFixtures.RESERVATION_1, TestFixtures.RESERVATION_2, TestFixtures.RESERVATION_3,
                TestFixtures.RESERVATION_4, TestFixtures.RESERVATION_5
        );

        assertThat(reservationRepository.findAll()).isEqualTo(expectedReservations);
    }

    @DisplayName("해당 id의 예약을 삭제한다")
    @Test
    void deleteById() {
        reservationRepository.deleteById(1L);

        List<Reservation> expectedReservations = List.of(
                TestFixtures.RESERVATION_2, TestFixtures.RESERVATION_3, TestFixtures.RESERVATION_4
        );

        assertThat(reservationRepository.findAll()).isEqualTo(expectedReservations);
    }

    @DisplayName("날짜와 테마가 일치하는 예약을 반환한다")
    @Test
    void findByDateAndThemeId() {
        List<Reservation> byDateAndThemeId = reservationRepository.findByDateAndThemeId(
                LocalDate.of(2024, 5, 12), 1L
        );

        assertThat(byDateAndThemeId).isEqualTo(List.of(TestFixtures.RESERVATION_4));
    }

    @DisplayName("일정 기간 이내의 예약을 반환한다")
    @Test
    void findByPeriod() {
        List<Reservation> byPeriod = reservationRepository.findByPeriod(
                LocalDate.of(2024, 5, 10),
                LocalDate.of(2024, 5, 12)
        );

        List<Reservation> expectedReservations = List.of(
                TestFixtures.RESERVATION_2, TestFixtures.RESERVATION_3, TestFixtures.RESERVATION_4
        );

        assertThat(byPeriod).isEqualTo(expectedReservations);
    }
}
