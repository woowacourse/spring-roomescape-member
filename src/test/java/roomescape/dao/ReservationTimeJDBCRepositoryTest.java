package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.ReservationTime;

@JdbcTest
@Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
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
        int expectedSize = 2;

        //when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        //then
        assertThat(reservationTimes.size()).isEqualTo(expectedSize);
    }

    @DisplayName("id로 예약 시간을 조회한다.")
    @Test
    void findReservationTimeByIdTest() {
        //when
        ReservationTime result = reservationTimeRepository.findById(1).get();

        //then
        assertThat(result.getId()).isEqualTo(1);
    }

    @DisplayName("id로 예약 시간을 삭제한다.")
    @Test
    void deleteReservationTimeByIdTest() {
        //given
        int expectedSize = 1;

        //when
        reservationTimeRepository.deleteById(2);

        //then
        assertThat(reservationTimeRepository.findAll().size()).isEqualTo(expectedSize);
    }

    @DisplayName("예약이 가능한 시간이 있다.")
    @Test
    void findAvailableTimesByThemeAndDate() {
        //when
        List<ReservationTime> result = reservationTimeRepository.getReferenceByDateAndTheme("2222-05-04", 1);

        //then
        assertThat(result).hasSize(2);
    }
}
