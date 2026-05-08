package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservationtime.domain.ReservationTime;

@JdbcTest(properties = "spring.sql.init.mode=never")
@Import(ReservationTimeRepository.class)
@Sql(scripts = "classpath:schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:reset-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeRepositoryTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    void 예약_시간을_저장하고_ID로_조회할_수_있다() {
        ReservationTime saved = reservationTimeRepository.save(LocalTime.of(10, 0));

        ReservationTime found = reservationTimeRepository.findById(saved.id()).orElseThrow();

        assertThat(found.id()).isEqualTo(saved.id());
        assertThat(found.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약_시간을_전체_조회할_수_있다() {
        reservationTimeRepository.save(LocalTime.of(10, 0));
        reservationTimeRepository.save(LocalTime.of(11, 0));

        List<ReservationTime> times = reservationTimeRepository.findAll();

        assertThat(times).hasSize(2);
        assertThat(times)
                .extracting(ReservationTime::startAt)
                .containsExactly(LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    @Test
    void ID로_예약_시간을_삭제할_수_있다() {
        ReservationTime saved = reservationTimeRepository.save(LocalTime.of(10, 0));

        reservationTimeRepository.delete(saved.id());

        assertThat(reservationTimeRepository.findAll()).isEmpty();
    }

    @Test
    void 예약_시간_ID가_없으면_빈_값을_반환한다() {
        assertThat(reservationTimeRepository.findById(999L)).isEmpty();
    }
}

