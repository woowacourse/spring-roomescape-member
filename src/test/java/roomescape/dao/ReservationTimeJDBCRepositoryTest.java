package roomescape.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;

@JdbcTest
class ReservationTimeJDBCRepositoryTest {
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new ReservationTimeJDBCRepository(jdbcTemplate);
    }

    @DisplayName("새로운 예약 시간을 저장한다.")
    @Test
    void saveReservationTime() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);

        //when
        ReservationTime result = reservationTimeRepository.save(reservationTime);

        //then
        assertThat(result.getId()).isNotZero();
    }

    @DisplayName("모든 예약 시간을 조회한다.")
    @Test
    void findAllReservationTimesTest() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);
        reservationTimeRepository.save(reservationTime);
        int expectedSize = 1;

        //when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        //then
        assertThat(reservationTimes.size()).isEqualTo(expectedSize);
    }

    @DisplayName("id로 예약 시간을 조회한다.")
    @Test
    void findReservationTimeByIdTest() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);
        ReservationTime target = reservationTimeRepository.save(reservationTime);

        //when
        ReservationTime result = reservationTimeRepository.findById(target.getId()).get();

        //then
        assertAll(
                () -> Assertions.assertThat(result.getId()).isEqualTo(target.getId()),
                () -> Assertions.assertThat(result.getStartAt()).isEqualTo(startAt)
        );
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void deleteReservationTimeByIdTest() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);
        ReservationTime target = reservationTimeRepository.save(reservationTime);
        int expectedSize = 0;

        //when
        reservationTimeRepository.deleteById(target.getId());

        //then
        assertThat(reservationTimeRepository.findAll().size()).isEqualTo(expectedSize);
    }
}
