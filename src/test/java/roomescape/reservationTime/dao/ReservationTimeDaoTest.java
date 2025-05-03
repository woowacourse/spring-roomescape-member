package roomescape.reservationTime.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.reservationTime.domain.ReservationTime;

@JdbcTest(properties = "spring.sql.init.mode=never")
@Import({ReservationTimeDao.class})
class ReservationTimeDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        JdbcTemplate jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();

        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time");

        jdbcTemplate.execute("""
                    CREATE TABLE reservation_time (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        start_at VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
                    );
                """);

        String insertSqlReservationTime = "INSERT INTO reservation_time(start_at) VALUES (:start_at)";
        namedParameterJdbcTemplate.update(
                insertSqlReservationTime,
                Map.of(
                        "start_at", "10:00"
                )
        );
    }

    @DisplayName("시간 내역을 조회하는 기능을 구현한다")
    @Test
    void findAll() {
        assertThat(reservationTimeDao.findAll()).hasSize(1);
    }

    @DisplayName("시간 내역을 아이디로 조회하는 기능을 구현한다")
    @Test
    void findById() {
        ReservationTime reservationTime = reservationTimeDao.findById(1L).get();

        assertThat(reservationTime.getId()).isEqualTo(1L);
    }

    @DisplayName("시간 내역을 추가하는 기능을 구현한다")
    @Test
    void add() {
        ReservationTime reservationTime = new ReservationTime(2L, LocalTime.of(11, 0));

        reservationTimeDao.add(reservationTime);

        assertThat(reservationTimeDao.findAll()).hasSize(2);
    }

    @DisplayName("시간 내역을 삭제하는 기능을 구현한다")
    @Test
    void deleteById() {
        reservationTimeDao.deleteById(1L);

        assertThat(reservationTimeDao.findAll()).hasSize(0);
    }
}
