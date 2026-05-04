package roomescape.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcTemplateReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 시간을_저장하면_id가_채워진_도메인을_반환한다() {
        ReservationTime toSave = new ReservationTime(null, LocalTime.of(10, 0));

        ReservationTime saved = reservationTimeRepository.addTime(toSave);

        assertThat(saved.id()).isNotNull();
        assertThat(saved.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 모든_시간을_조회한다() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00");

        List<ReservationTime> times = reservationTimeRepository.findAllReservationTimes();

        assertThat(times).hasSize(2);
    }

    @Test
    void 시간이_없으면_빈_리스트를_반환한다() {
        List<ReservationTime> times = reservationTimeRepository.findAllReservationTimes();

        assertThat(times).isEmpty();
    }

    @Test
    void id로_시간을_조회한다() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        ReservationTime time = reservationTimeRepository.findById(1L).get();

        assertThat(time.id()).isEqualTo(1L);
        assertThat(time.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void id로_시간을_삭제한다() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        reservationTimeRepository.deleteTime(1L);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM reservation_time", Integer.class);
        assertThat(count).isEqualTo(0);
    }
}
