package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationTimeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약시간 목록을 전부 조회할 수 있다.")
    @Test
    void getAllTimes() {
        List<ReservationTimeResponse> responses = reservationTimeService.getAllTimes();

        // test-data.sql 기준 10:00 ~ 22:00
        assertThat(responses).hasSize(13);

        assertThat(responses.getFirst().startAt()).isEqualTo("10:00");
        assertThat(responses.getLast().startAt()).isEqualTo("22:00");
    }

    @DisplayName("존재하지 않은 예약 시간을 생성할 수 있다.")
    @Test
    void createTimeSuccess() {
        LocalTime startAt = LocalTime.of(23, 0);

        ReservationTime createdTime = reservationTimeService.createTime(startAt);

        assertThat(createdTime.getId()).isNotNull();

        ReservationTime foundTime = reservationTimeRepository.findById(createdTime.getId())
                .orElseThrow();

        assertThat(foundTime.getStartAt()).isEqualTo(startAt);
    }

    @DisplayName("이미 존재하는 예약 시간은 생성할 수 없다.")
    @Test
    void createTimeFailByDuplicatedTime() {
        // test-data.sql 기준 이미 존재
        LocalTime startAt = LocalTime.of(11, 0);

        assertThatThrownBy(() -> reservationTimeService.createTime(startAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 예약 시간입니다.");
    }

    @DisplayName("예약에 사용 중인 시간은 삭제할 수 없다.")
    @Test
    void deleteTimeFailByInUse() {
        // test-data.sql 기준:
        // time_id=3 (12:00)은 여러 예약에서 사용 중
        assertThatThrownBy(() -> reservationTimeService.deleteTime(3L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약에 사용 중인 시간은 삭제할 수 없습니다.");
    }

    @DisplayName("예약에 사용되지 않은 시간은 삭제할 수 있다.")
    @Test
    void deleteTimeSuccess() {
        // 22:00(id=13)은 기본 데이터 기준 예약 없음
        reservationTimeService.deleteTime(13L);

        assertThat(reservationTimeRepository.findById(13L)).isEmpty();
    }

    @DisplayName("아이디로 예약 시간을 조회할 수 있다.")
    @Test
    void getTimeSuccess() {
        // test-data.sql 기준 id=1 -> 10:00
        ReservationTime result = reservationTimeService.getTime(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("존재하지 않는 예약 시간 조회 시 예외가 발생한다.")
    @Test
    void getTimeFailByMissingTime() {
        assertThatThrownBy(() -> reservationTimeService.getTime(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }
}