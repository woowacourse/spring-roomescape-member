package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.ReservationTime;

class ReservationTimeDaoTest extends DaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    void findAll_전체_시간_조회() {
        List<ReservationTime> times = reservationTimeDao.findAll();

        assertThat(times).hasSize(13);
    }

    @Test
    void findById_존재하는_id이면_반환() {
        Optional<ReservationTime> result = reservationTimeDao.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void findById_존재하지_않는_id이면_empty() {
        Optional<ReservationTime> result = reservationTimeDao.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void existsByStartAt_존재하면_true() {
        assertThat(reservationTimeDao.existsByStartAt(LocalTime.of(10, 0))).isTrue();
    }

    @Test
    void existsByStartAt_없으면_false() {
        assertThat(reservationTimeDao.existsByStartAt(LocalTime.of(23, 0))).isFalse();
    }

    @Test
    void save_시간_저장() {
        ReservationTime saved = reservationTimeDao.save(LocalTime.of(23, 0));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStartAt()).isEqualTo(LocalTime.of(23, 0));
    }

    @Test
    void delete_시간_삭제() {
        // time_id=12 (21:00)은 예약이 없으므로 FK 제약 없이 삭제 가능
        reservationTimeDao.delete(12L);

        assertThat(reservationTimeDao.findById(12L)).isEmpty();
    }
}
