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
                LocalDate.now().plusDays(8), 1L
        );

        assertThat(byDateAndThemeId).isEqualTo(List.of(TestFixtures.RESERVATION_4));
    }

    @DisplayName("일정 기간 이내의 예약을 반환한다")
    @Test
    void findByPeriod() {
        List<Reservation> byPeriod = reservationRepository.findByPeriod(
                LocalDate.now().plusDays(6),
                LocalDate.now().plusDays(8)
        );

        List<Reservation> expectedReservations = List.of(
                TestFixtures.RESERVATION_2, TestFixtures.RESERVATION_3, TestFixtures.RESERVATION_4
        );

        assertThat(byPeriod).isEqualTo(expectedReservations);
    }

    @DisplayName("멤버id, 테마id, 시작 날짜, 끝 날짜의 조건을 만족하는 예약 목록을 반환한다")
    @Test
    void searchReservations() {
        LocalDate today = LocalDate.now();
        List<Reservation> reservations = reservationRepository.searchReservations(
                2L, 1L, today.plusDays(5), today.plusDays(7)
        );

        assertThat(reservations).isEqualTo(List.of(TestFixtures.RESERVATION_2));
    }
}
