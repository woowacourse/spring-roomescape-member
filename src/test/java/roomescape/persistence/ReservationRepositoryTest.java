package roomescape.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.business.Reservation;
import roomescape.business.ReservationTime;

@JdbcTest
@Import({ReservationRepository.class, ReservationTimeRepository.class})
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    private ReservationTime reservationTime;

    @BeforeEach
    void setUp() {
        Long reservationTimeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now()));
        reservationTime = reservationTimeRepository.findById(reservationTimeId);
    }

    @DisplayName("예약 객체를 추가한다")
    @Test
    void add() {
        // given
        Reservation reservation = new Reservation("예약자", LocalDate.now(), reservationTime);

        // when
        Long id = reservationRepository.add(reservation);

        // then
        Assertions.assertThat(id).isEqualTo(reservationRepository.findById(id).getId());
    }

    @DisplayName("모든 예약 객체를 반환한다")
    @Test
    void findAll() {
        // given
        reservationRepository.add(new Reservation("예약자", LocalDate.now(), reservationTime));

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        Assertions.assertThat(reservations).hasSize(1);
    }

    @DisplayName("아이디로 예약 객체를 찾아 반환한다")
    @Test
    void findById() {
        // given
        Long id = reservationRepository.add(new Reservation("예약자", LocalDate.now(), reservationTime));

        // when
        Reservation findReservation = reservationRepository.findById(id);

        // then
        Assertions.assertThat(findReservation.getId()).isEqualTo(id);
    }

    @DisplayName("예약 객체를 삭제한다")
    @Test
    void delete() {
        // given
        Long id = reservationRepository.add(new Reservation("예약자", LocalDate.now(), reservationTime));

        // when
        reservationRepository.delete(id);

        // then
        Assertions.assertThat(reservationRepository.findAll()).isEmpty();
    }
}