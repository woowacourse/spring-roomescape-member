package roomescape.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
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

/*
 * 테스트 데이터베이스 초기 데이터
 * {ID=1, NAME=브라운, DATE=2024-05-04, TIME={ID=1, START_AT="10:00"}}
 * {ID=2, NAME=엘라, DATE=2024-05-04, TIME={ID=2, START_AT="11:00"}}
 * {ID=3, NAME=릴리, DATE=2023-08-05, TIME={ID=2, START_AT="11:00"}}
 *
 * 테스트 데이터베이스 초기 데이터
 * {ID=1, START_AT=10:00}
 * {ID=2, START_AT=11:00}
 * {ID=3, START_AT=13:00}
 */
@JdbcTest
@Sql(scripts = "/reset_test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class ReservationTimeRepositoryTest {

    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new ReservationTimeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("모든 예약 시간 데이터를 가져온다.")
    void findAll() {
        // when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        // then
        assertThat(reservationTimes).hasSize(3);
    }

    @Test
    @DisplayName("특정 예약 시간 id의 데이터를 조회한다.")
    void findById() {
        // given
        ReservationTime targetTime = new ReservationTime(2L, LocalTime.parse("11:00"));

        // when
        ReservationTime findReservationTime = reservationTimeRepository.findById(targetTime.getId()).orElseThrow();

        // then
        assertThat(findReservationTime).isEqualTo(targetTime);
    }

    @Test
    @DisplayName("예약 시간을 생성한다.")
    void create() {
        // given
        LocalTime startAt = LocalTime.parse("13:00");
        ReservationTime inputData = new ReservationTime(null, startAt);

        // when
        ReservationTime createdTime = reservationTimeRepository.create(inputData);

        // then
        assertThat(createdTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void delete() {
        // given
        Long id = 3L;

        // when
        reservationTimeRepository.removeById(id);

        // then
        assertThat(reservationTimeRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("동일한 예약 시간이 존재하는지 확인한다.")
    void hasDuplicateTime() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.parse("10:00"));

        // when
        boolean result = reservationTimeRepository.hasDuplicateTime(reservationTime);

        // then
        assertThat(result).isTrue();
    }
}
