package roomescape.reservation.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class JdbcReservationReservationTimeRepositoryTest {
    @Autowired
    private JdbcReservationTimeRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 시간_저장_레포지토리_테스트() {
        ReservationTime savedReservationTime = repository.save(LocalTime.of(15, 40));
        Long id = jdbcTemplate.queryForObject("SELECT id FROM reservation_time LIMIT 1", Long.class);

        assertThat(savedReservationTime.getId()).isEqualTo(id);
        assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.of(15, 40));
    }

    @Test
    void 전체_시간_조회_레포지토리_테스트() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "16:00");

        List<ReservationTime> reservationTimes = repository.findAll();

        assertThat(reservationTimes).hasSize(2);
        assertThat(reservationTimes)
                .extracting(ReservationTime::getStartAt)
                .containsExactly(LocalTime.of(15, 00), LocalTime.of(16, 00));
    }

    @Test
    void 시간_삭제_레포지토리_테스트(){
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)","15:00");
        Long id = jdbcTemplate.queryForObject("SELECT id FROM reservation_time LIMIT 1", Long.class);

        repository.deleteById(id);
        int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);

        assertThat(rowCount).isEqualTo(0);
    }
}
