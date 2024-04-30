package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@JdbcTest
@Import({ReservationRepository.class, ReservationTimeRepository.class})
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("DB 저장 테스트")
    void saveTest() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        Long timeId = reservationTimeRepository.save(reservationTime);
        Reservation reservation = new Reservation(1L, new Name("hogi"), LocalDate.now(), reservationTime);
        Long saveId = reservationRepository.save(reservation);

        assertThat(saveId).isEqualTo(1L);
    }

    @Test
    @DisplayName("DB 조회 테스트")
    void findAllTest() {
        Long timeId = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId).get();
        Reservation reservation1 = new Reservation(new Name("hogi"), LocalDate.now(), reservationTime);
        Reservation reservation2 = new Reservation(new Name("kaki"), LocalDate.now(), reservationTime);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("id 값을 받아 Reservation 반환")
    void findByIdTest() {
        Long timeId = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId).get();
        Reservation reservation = new Reservation(new Name("hogi"), LocalDate.now(), reservationTime);
        Long saveId = reservationRepository.save(reservation);

        Reservation findReservation = reservationRepository.findById(saveId).get();

        assertThat(findReservation.getId()).isEqualTo(saveId);
    }

    @Test
    @DisplayName("DB 삭제 테스트")
    void deleteTest() {
        Long timeId = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId).get();
        Reservation reservation = new Reservation(new Name("hogi"), LocalDate.now(), reservationTime);
        Long saveId = reservationRepository.save(reservation);

        reservationRepository.delete(saveId);
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.size()).isEqualTo(0);
    }
}
