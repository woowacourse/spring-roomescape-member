package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.entity.Reservation;

class ReservationRepositoryTest {

    private ReservationRepository reservationRepository;

    @BeforeEach
    void init() {
        reservationRepository = new ReservationRepositoryImpl();
    }

    @DisplayName("초기 ID의 값은 1이다.")
    @Test
    void test1() {
        long result = reservationRepository.generateId();

        assertThat(result).isEqualTo(1);
    }

    @DisplayName("ID의 값은 순차적으로 올라간다.")
    @Test
    void test2() {
        for (int i = 1; i <= 5000; i++) {
            long result = reservationRepository.generateId();

            assertThat(result).isEqualTo(i);
        }
    }

    @DisplayName("예약 정보를 저장한다.")
    @Test
    void test3() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(1, "꾹", now);

        // when
        Reservation result = reservationRepository.save(reservation);

        // then
        assertThat(result).isEqualTo(reservation);
    }

    @DisplayName("id가 같다면 해당 예약 정보로 변경한다.")
    @Test
    void test4() {
        // given
        LocalDateTime now = LocalDateTime.now();

        String originalName = "꾹";
        String changedName = "드라고";
        Reservation reservation = new Reservation(1, originalName, now);
        reservationRepository.save(reservation);

        Reservation updateReservation = new Reservation(1, changedName, now);

        // when
        Reservation result = reservationRepository.save(updateReservation);

        // then
        assertThat(result).isEqualTo(updateReservation);
    }

    @DisplayName("id로 예약 정보를 가져온다")
    @Test
    void test5() {
        // given
        long id = 2;
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(id, "꾹", now);
        reservationRepository.save(reservation);

        // when
        Optional<Reservation> result = reservationRepository.findById(id);

        // then
        assertThat(result).contains(reservation);
    }

    @DisplayName("모든 예약 정보를 가져온다.")
    @Test
    void test6() {
        // given
        LocalDateTime now = LocalDateTime.now();

        List<Reservation> reservations = List.of(
                new Reservation(1, "꾹", now),
                new Reservation(2, "꾹", now),
                new Reservation(3, "꾹", now)
        );

        for (Reservation reservation : reservations) {
            reservationRepository.save(reservation);
        }

        // when
        List<Reservation> result = reservationRepository.findAll();

        // then
        assertThat(result).containsExactlyInAnyOrderElementsOf(reservations);
    }

    @DisplayName("예약 정보를 삭제한다.")
    @Test
    void test7() {
        // given
        long id = 1;
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(id, "꾹", now);
        reservationRepository.save(reservation);

        // when
        reservationRepository.deleteById(id);

        // then
        Optional<Reservation> result = reservationRepository.findById(id);
        assertThat(result).isEmpty();
    }
}
