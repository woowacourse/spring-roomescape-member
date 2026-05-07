package roomescape.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcTemplateReservationTimeRepository.class)
class JdbcTemplateReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 시간을_저장하면_id가_채워진_도메인을_반환한다() {
        ReservationTime saved = reservationTimeRepository.addTime(new ReservationTime(null, LocalTime.of(10, 0)));

        assertThat(saved.id()).isNotNull();
        assertThat(saved.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 모든_시간을_조회한다() {
        addTime(LocalTime.of(10, 0));
        addTime(LocalTime.of(11, 0));

        List<ReservationTime> times = reservationTimeRepository.findAllReservationTimes();

        assertThat(times).hasSize(2);
    }

    private long addTime(LocalTime startAt) {
        return reservationTimeRepository.addTime(new ReservationTime(null, startAt)).id();
    }

    @Test
    void 시간이_없으면_빈_리스트를_반환한다() {
        List<ReservationTime> times = reservationTimeRepository.findAllReservationTimes();

        assertThat(times).isEmpty();
    }

    @Test
    void id로_시간을_조회한다() {
        long id = addTime(LocalTime.of(10, 0));

        ReservationTime time = reservationTimeRepository.findById(id).get();

        assertThat(time.id()).isEqualTo(id);
        assertThat(time.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void id로_시간을_삭제한다() {
        long id = addTime(LocalTime.of(10, 0));

        reservationTimeRepository.deleteTime(id);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM reservation_time", Integer.class);
        assertThat(count).isEqualTo(0);
    }
}
