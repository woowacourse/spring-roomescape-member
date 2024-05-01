package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class JdbcReservationRepositoryImplTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationTime reservationTime;

    @BeforeEach
    void saveTime() {
        reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(5, 30)));
    }

    @DisplayName("예약 정보를 DB에 저장한다.")
    @Test
    void save() {
        LocalDate date = LocalDate.MAX;
        Reservation reservation = new Reservation("브리", date, reservationTime);

        Reservation actual = reservationRepository.save(reservation);
        Reservation expected = new Reservation(actual.getId(), reservation.getName(), reservation.getDate(),
            reservation.getTime());

        assertAll(
            () -> assertEquals(expected.getDate(), actual.getDate()),
            () -> assertEquals(expected.getTime(), actual.getTime()),
            () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @DisplayName("모든 예약 정보를 DB에서 조회한다.")
    @Test
    void findAll() {
        LocalDate date = LocalDate.MAX;
        Reservation save1 = reservationRepository.save(new Reservation("브리", date, reservationTime));
        Reservation save2 = reservationRepository.save(new Reservation("솔라", date, reservationTime));

        List<Reservation> actual = reservationRepository.findAll();
        List<Reservation> expected = List.of(save1, save2);

        assertAll(
            () -> assertEquals(2, actual.size()),
            () -> assertEquals(expected.get(0).getId(), actual.get(0).getId()),
            () -> assertEquals(expected.get(0).getName(), actual.get(0).getName()),
            () -> assertEquals(expected.get(1).getId(), actual.get(1).getId()),
            () -> assertEquals(expected.get(1).getName(), actual.get(1).getName())
        );
    }

    @DisplayName("id값을 통해 예약 정보를 DB에서 삭제한다.")
    @Test
    void deleteById() {
        LocalDate date = LocalDate.MAX;
        Reservation reservation = new Reservation("브리", date, reservationTime);
        Reservation save = reservationRepository.save(reservation);

        reservationRepository.deleteById(save.getId());

        assertTrue(reservationRepository.findAll().isEmpty());
    }

    @DisplayName("time_id값을 통해 예약 수를 가져온다.")
    @Test
    void countByTimeId() {
        Reservation reservation1 = new Reservation("brown1", LocalDate.MAX, reservationTime);
        Reservation reservation2 = new Reservation("brown2", LocalDate.MIN, reservationTime);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        long actual = reservationRepository.countByTimeId(reservationTime.getId());

        assertThat(actual).isEqualTo(2L);
    }

    @DisplayName("date와 time_id로 예약 수를 가져온다.")
    @Test
    void countByDateAndTimeId() {
        LocalDate date = LocalDate.parse("2040-01-01");
        Reservation reservation = new Reservation("brown1", date, reservationTime);
        reservationRepository.save(reservation);
        long count = reservationRepository.countByDateAndTimeId(date, reservationTime.getId());
        assertThat(count).isEqualTo(1L);
    }
}
