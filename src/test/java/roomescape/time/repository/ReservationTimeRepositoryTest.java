package roomescape.time.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationUserTime;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/data.sql", "/sample.sql"})
class ReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("예약 시간을 생성할 수 있다.")
    void save() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(23, 59));
        assertThat(reservationTimeRepository.save(reservationTime)).isEqualTo(11L);
    }

    @Test
    @DisplayName("특정 예약 시간을 조회할 수 있다.")
    void findById() {
        assertThat(reservationTimeRepository.findById(1L)).isEqualTo(new ReservationTime(1L, LocalTime.of(9, 0)));
    }

    @Test
    @DisplayName("모든 예약 시간을 조회할 수 있다.")
    void findAll() {
        assertAll(
                () -> assertThat(reservationTimeRepository.findAll()
                        .size()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("특정 예약 시간을 삭제할 수 있다.")
    void deleteById() {
        assertThat(reservationTimeRepository.deleteById(10L)).isEqualTo(1);
    }

    @Test
    @DisplayName("날짜와 테마를 통해 가능한 예약을 찾을 수 있다.")
    void findAvailableTime() {
        List<ReservationUserTime> userTimes = List.of(
                new ReservationUserTime(1L, "09:00:00", false),
                new ReservationUserTime(2L, "10:00:00", true),
                new ReservationUserTime(3L, "11:00:00", false),
                new ReservationUserTime(4L, "12:00:00", false),
                new ReservationUserTime(5L, "13:00:00", false),
                new ReservationUserTime(6L, "14:00:00", false),
                new ReservationUserTime(7L, "15:00:00", false),
                new ReservationUserTime(8L, "16:00:00", false),
                new ReservationUserTime(9L, "17:00:00", false),
                new ReservationUserTime(10L, "18:00:00", false)
        );

        assertThat(reservationTimeRepository.findAvailableTime("2024-04-24", 2)).isEqualTo(userTimes);
    }
}
