package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
@Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
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
        reservationTimeRepository.deleteById(2);
    }

    @DisplayName("모든 예약 시간을 조회한다.")
    @Test
    void findAllReservationTimesTest() {
        //given
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
                () -> assertThat(result.getId()).isEqualTo(target.getId()),
                () -> assertThat(result.getStartAt()).isEqualTo(startAt)
        );
        reservationTimeRepository.deleteById(2);
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void deleteReservationTimeByIdTest() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);
        ReservationTime target = reservationTimeRepository.save(reservationTime);
        int expectedSize = 1;

        //when
        reservationTimeRepository.deleteById(target.getId());

        //then
        assertThat(reservationTimeRepository.findAll().size()).isEqualTo(expectedSize);
    }

    @DisplayName("주어진 시간이 이미 존재한다.")
    @Test
    void existsTime() {
        //when
        boolean result = reservationTimeRepository.existsByStartAt("12:00");

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 시간이 존재하지 않는다.")
    @Test
    void notExistsTime() {
        //when
        boolean result = reservationTimeRepository.existsByStartAt("10:00");

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("예약이 가능한 시간이 없다.")
    @Test
    void findNoAvailableTimesByThemeAndDate() {
        //when
        List<ReservationTime> result = reservationTimeRepository.findByDateAndTheme("2222-05-04", 1);

        //then
        assertThat(result).hasSize(0);
    }

    @DisplayName("예약이 가능한 시간이 있다.")
    @Test
    void findAvailableTimesByThemeAndDate() {
        //when
        List<ReservationTime> result = reservationTimeRepository.findByDateAndTheme("2222-05-01", 1);

        //then
        assertThat(result).hasSize(1);
    }
}
