package roomescape.time.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.ReservationTimeStatus;

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
    void create() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(23, 59));
        assertThat(reservationTimeRepository.create(reservationTime)).isEqualTo(11L);
    }

    @Test
    @DisplayName("특정 예약 시간을 조회할 수 있다.")
    void find() {
        assertThat(reservationTimeRepository.find(1L)).isEqualTo(new ReservationTime(1L, LocalTime.of(9, 0)));
    }

    @Test
    @DisplayName("모든 예약 시간을 조회할 수 있다.")
    void readAll() {
        assertAll(
                () -> assertThat(reservationTimeRepository.readAll()
                        .size()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("특정 예약 시간을 삭제할 수 있다.")
    void delete() {
        assertThat(reservationTimeRepository.delete(10L)).isEqualTo(1);
    }

    @Test
    @DisplayName("날짜와 테마를 통해 가능한 예약을 찾을 수 있다.")
    void findAvailableTime() {
        List<ReservationTimeStatus> userTimes = List.of(
                new ReservationTimeStatus(1L, LocalTime.of(9, 0), false),
                new ReservationTimeStatus(2L, LocalTime.of(10, 0), true),
                new ReservationTimeStatus(3L, LocalTime.of(11, 0), false),
                new ReservationTimeStatus(4L, LocalTime.of(12, 0), false),
                new ReservationTimeStatus(5L, LocalTime.of(13, 0), false),
                new ReservationTimeStatus(6L, LocalTime.of(14, 0), false),
                new ReservationTimeStatus(7L, LocalTime.of(15, 0), false),
                new ReservationTimeStatus(8L, LocalTime.of(16, 0), false),
                new ReservationTimeStatus(9L, LocalTime.of(17, 0), false),
                new ReservationTimeStatus(10L, LocalTime.of(18, 0), false)
        );

        assertThat(reservationTimeRepository.findAvailableTime("2024-04-24", 2)).isEqualTo(userTimes);
    }
}
