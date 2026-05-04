package roomescape.time.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(ReservationTimeRepository.class)
class ReservationTimeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation_time");
    }

    @Test
    void 시간을_저장하면_생성된_id와_시작_시간을_반환한다() {
        ReservationTime saved = reservationTimeRepository.save(LocalTime.of(10, 0));

        assertThat(saved.getId()).isPositive();
        assertThat(saved.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 존재하는_id로_조회하면_예약_시간을_Optional로_반환한다() {
        ReservationTime saved = reservationTimeRepository.save(LocalTime.of(11, 30));

        Optional<ReservationTime> result = reservationTimeRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getStartAt()).isEqualTo(LocalTime.of(11, 30));
    }

    @Test
    void 존재하지_않는_id로_조회하면_빈_Optional을_반환한다() {
        Optional<ReservationTime> result = reservationTimeRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void 여러_시간을_저장한_뒤_전체_조회하면_모든_시간을_반환한다() {
        reservationTimeRepository.save(LocalTime.of(10, 0));
        reservationTimeRepository.save(LocalTime.of(11, 0));

        List<ReservationTime> result = reservationTimeRepository.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void 존재하는_id로_삭제하면_해당_시간이_삭제된다() {
        ReservationTime saved = reservationTimeRepository.save(LocalTime.of(10, 0));

        reservationTimeRepository.remove(saved.getId());

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM reservation_time WHERE id = ?", Integer.class, saved.getId());
        assertThat(count).isZero();
    }
}
