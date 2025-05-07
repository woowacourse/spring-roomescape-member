package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.ReservationTime;
import roomescape.domain.repository.ReservationTimeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    void 예약_시간을_추가할_수_있다() {

        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));

        // when
        reservationTimeRepository.create(reservationTime);
        List<ReservationTime> reservationTimeDaoAll = reservationTimeRepository.findAll();

        // then
        assertThat(reservationTimeDaoAll.size()).isEqualTo(1);
    }

    @Test
    void 예약_시간을_조회할_수_있다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));

        // when
        reservationTimeRepository.create(reservationTime);
        List<ReservationTime> reservationTimeDaoAll = reservationTimeRepository.findAll();

        // then
        assertThat(reservationTimeDaoAll.getFirst().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약_시간을_삭제할_수_있다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        reservationTimeRepository.create(reservationTime);
        int beforeSize = reservationTimeRepository.findAll().size();

        // when
        reservationTimeRepository.delete(1L);
        int afterSize = reservationTimeRepository.findAll().size();

        // then
        assertThat(beforeSize).isEqualTo(1);
        assertThat(afterSize).isEqualTo(0);
    }
}
