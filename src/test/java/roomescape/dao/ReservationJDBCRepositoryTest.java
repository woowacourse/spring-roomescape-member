package roomescape.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@JdbcTest
class ReservationJDBCRepositoryTest {
    private final JdbcTemplate jdbcTemplate;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private String date;
    private ReservationTime reservationTime;

    @Autowired
    ReservationJDBCRepositoryTest(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationJDBCRepository(jdbcTemplate);
        reservationTimeRepository = new ReservationTimeJDBCRepository(jdbcTemplate);
        date = LocalDate.now().plusDays(1).toString();
        String startAt = LocalTime.now().toString();
        reservationTime = reservationTimeRepository.save(new ReservationTime(startAt));
    }

    @DisplayName("새로운 예약을 저장한다.")
    @Test
    void saveReservation() {
        //given
        Reservation reservation = new Reservation("브라운", date, reservationTime);

        //when
        Reservation result = reservationRepository.save(reservation);

        //then
        assertThat(result.getId()).isNotZero();
    }

    @DisplayName("모든 예약 내역을 조회한다.")
    @Test
    void findAllReservationTest() {
        //given
        Reservation reservation = new Reservation("브라운", date, reservationTime);
        reservationRepository.save(reservation);
        int expectedSize = 1;

        //when
        List<Reservation> reservations = reservationRepository.findAll();

        //then
        assertThat(reservations.size()).isEqualTo(expectedSize);
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void deleteReservationByIdTest() {
        //given
        Reservation reservation = new Reservation("브라운", date, reservationTime);
        Reservation target = reservationRepository.save(reservation);
        int expectedSize = 0;

        //when
        reservationRepository.deleteById(target.getId());

        //then
        assertThat(reservationRepository.findAll().size()).isEqualTo(expectedSize);
    }
}
