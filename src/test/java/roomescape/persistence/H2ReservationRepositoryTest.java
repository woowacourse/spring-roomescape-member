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
@Import({H2ReservationRepository.class, H2ReservationTimeRepository.class})
class H2ReservationRepositoryTest {

    @Autowired
    private H2ReservationRepository h2ReservationRepository;
    @Autowired
    private H2ReservationTimeRepository h2ReservationTimeRepository;

    private ReservationTime reservationTime;

    @BeforeEach
    void setUp() {
        Long reservationTimeId = h2ReservationTimeRepository.add(new ReservationTime(LocalTime.now()));
        reservationTime = h2ReservationTimeRepository.findById(reservationTimeId);
    }

    @DisplayName("예약 객체를 추가한다")
    @Test
    void add() {
        // given
        Reservation reservation = new Reservation("예약자", LocalDate.now(), reservationTime);

        // when
        Long id = h2ReservationRepository.add(reservation);

        // then
        Assertions.assertThat(id).isEqualTo(h2ReservationRepository.findById(id).getId());
    }

    @DisplayName("모든 예약 객체를 반환한다")
    @Test
    void findAll() {
        // given
        h2ReservationRepository.add(new Reservation("예약자", LocalDate.now(), reservationTime));

        // when
        List<Reservation> reservations = h2ReservationRepository.findAll();

        // then
        Assertions.assertThat(reservations).hasSize(1);
    }

    @DisplayName("아이디로 예약 객체를 찾아 반환한다")
    @Test
    void findById() {
        // given
        Long id = h2ReservationRepository.add(new Reservation("예약자", LocalDate.now(), reservationTime));

        // when
        Reservation findReservation = h2ReservationRepository.findById(id);

        // then
        Assertions.assertThat(findReservation.getId()).isEqualTo(id);
    }

    @DisplayName("예약 객체를 삭제한다")
    @Test
    void delete() {
        // given
        Long id = h2ReservationRepository.add(new Reservation("예약자", LocalDate.now(), reservationTime));

        // when
        h2ReservationRepository.delete(id);

        // then
        Assertions.assertThat(h2ReservationRepository.findAll()).isEmpty();
    }
}