//package roomescape.schedule.service;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import roomescape.schedule.dto.ScheduleRequest;
//import roomescape.schedule.dto.ScheduleResponse;
//
//import java.time.LocalTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class ReservationTimeServiceTest {
//
//    @Autowired
//    private ScheduleService reservationTimeService;
//
//    @Test
//    @DisplayName("새로운 예약 시간을 생성하고 실제 DB에 저장된다.")
//    void create() {
//        // given
//        LocalTime startTime = LocalTime.of(14, 0);
//        ScheduleRequest request = new ScheduleRequest(startTime);
//
//        // when
//        ScheduleResponse response = reservationTimeService.create(request);
//
//        // then
//        assertThat(response.getId()).isNotNull();
//        assertThat(response.getStartAt()).isEqualTo(startTime);
//
//        List<ScheduleResponse> allTimes = reservationTimeService.findAll();
//        assertThat(allTimes).anyMatch(time -> time.getStartAt().equals(startTime));
//    }
//
//    @Test
//    @DisplayName("실제 DB에 저장된 모든 예약 시간을 조회한다.")
//    void findAll() {
//        // given
//        reservationTimeService.create(new ScheduleRequest(LocalTime.of(10, 0)));
//        reservationTimeService.create(new ScheduleRequest(LocalTime.of(13, 0)));
//
//        // when
//        List<ScheduleResponse> responses = reservationTimeService.findAll();
//
//        // then
//        assertThat(responses).hasSize(2);
//        assertThat(responses).extracting("startAt")
//                .containsExactly(LocalTime.of(10, 0), LocalTime.of(13, 0));
//    }
//
//    @Test
//    @DisplayName("실제 DB에서 ID를 이용해 예약 시간을 삭제한다.")
//    void delete() {
//        // given
//        ScheduleResponse saved = reservationTimeService.create(new ScheduleRequest(LocalTime.of(15, 0)));
//        Long targetId = saved.getId();
//
//        // when
//        int result = reservationTimeService.delete(targetId);
//
//        // then
//        assertThat(result).isEqualTo(1);
//
//        List<ScheduleResponse> remaining = reservationTimeService.findAll();
//        assertThat(remaining).extracting("id").doesNotContain(targetId);
//    }
//}
