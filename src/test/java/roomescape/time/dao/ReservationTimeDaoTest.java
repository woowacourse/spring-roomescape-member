package roomescape.time.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationUserTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql", "/data.sql"})
class ReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    @DisplayName("예약 시간을 생성할 수 있다.")
    void save() {
        ReservationTime reservationTime = new ReservationTime("23:59");
        assertThat(reservationTimeDao.save(reservationTime)).isEqualTo(11L);
    }

    @Test
    @DisplayName("특정 예약 시간을 조회할 수 있다.")
    void getById() {
        assertThat(reservationTimeDao.getById(1L)).isEqualTo(new ReservationTime(1L, "09:00"));
    }

    @Test
    @DisplayName("모든 예약 시간을 조회할 수 있다.")
    void findAll() {
        assertAll(
                () -> assertThat(reservationTimeDao.findAll()
                                                   .size()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("특정 예약 시간을 삭제할 수 있다.")
    void deleteById() {
        assertThat(reservationTimeDao.deleteById(10L)).isEqualTo(1);
    }

    @Test
    @DisplayName("날짜와 테마를 통해 가능한 예약을 찾을 수 있다.")
    void findAvailableTime() {
        List<ReservationUserTime> userTimes = List.of(
                new ReservationUserTime(1L, "09:00", false),
                new ReservationUserTime(2L, "10:00", true),
                new ReservationUserTime(3L, "11:00", false),
                new ReservationUserTime(4L, "12:00", false),
                new ReservationUserTime(5L, "13:00", false),
                new ReservationUserTime(6L, "14:00", false),
                new ReservationUserTime(7L, "15:00", false),
                new ReservationUserTime(8L, "16:00", false),
                new ReservationUserTime(9L, "17:00", false),
                new ReservationUserTime(10L, "18:00", false)
        );
        assertThat(reservationTimeDao.findAvailableTime("2024-04-24", 2)).isEqualTo(userTimes);
    }

    @Test
    @DisplayName("특정 예약 시간이 이미 존재하는 지 알 수 있다.")
    void checkExistTime() {
        assertThat(reservationTimeDao.checkExistTime(new ReservationTime(1L, "09:00"))).isTrue();
    }
}
