package roomescape.time.repository;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import roomescape.time.domain.ReservationTime;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcTimeRepositoryTest {

    @Autowired
    private JdbcTimeRepository timeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void save() {
        ReservationTime saved = timeRepository.save(LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getStartAt()).isEqualTo(LocalTime.of(9, 0));
        assertThat(saved.getEndAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void findByStartAt() {
        timeRepository.save(LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertThat(timeRepository.findByStartAt(LocalTime.of(9, 0)))
                .get()
                .extracting(ReservationTime::getEndAt)
                .isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void reservation_time은_TIME_타입으로_관리한다() {
        String dataType = jdbcTemplate.queryForObject(
                """
                        SELECT DATA_TYPE
                        FROM INFORMATION_SCHEMA.COLUMNS
                        WHERE TABLE_NAME = 'RESERVATION_TIME'
                            AND COLUMN_NAME = 'START_TIME'
                        """,
                String.class
        );

        assertThat(dataType).isEqualTo("TIME");
    }
}
