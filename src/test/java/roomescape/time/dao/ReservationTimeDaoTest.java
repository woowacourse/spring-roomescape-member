package roomescape.time.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.time.domain.ReservationTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    private ReservationTime reservationTime = new ReservationTime(1L, "23:59");

    @Test
    @DisplayName("예약 시간을 생성할 수 있다.")
    void save() {
        assertThat(reservationTimeDao.save(reservationTime)).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 예약 시간을 조회할 수 있다.")
    void findById() {
        save();
        assertThat(reservationTimeDao.findById(1L)).isEqualTo(reservationTime);
    }

    @Test
    @DisplayName("모든 예약 시간을 조회할 수 있다.")
    void findAll() {
        save();
        assertAll(
                () -> assertThat(reservationTimeDao.findAll()
                        .get(0)).isEqualTo(reservationTime),
                () -> assertThat(reservationTimeDao.findAll()
                        .size()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("특정 예약 시간을 삭제할 수 있다.")
    void deleteById() {
        save();
        assertThat(reservationTimeDao.deleteById(1L)).isEqualTo(1);
    }
}
