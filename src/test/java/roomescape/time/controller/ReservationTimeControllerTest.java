package roomescape.time.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.ResponseReservationTime;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalTime;
import java.util.List;

class ReservationTimeControllerTest {

    private final FakeReservationTimeService fakeReservationTimeService;
    private final ReservationTimeController reservationTimeController;

    public ReservationTimeControllerTest() {
        this.fakeReservationTimeService = new FakeReservationTimeService();
        this.reservationTimeController = new ReservationTimeController(fakeReservationTimeService);
    }

    @Test
    void 시간_목록_조회_요청을_Service에_전달하고_결과를_반환한다() {
        List<ReservationTime> times = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0))
        );
        fakeReservationTimeService.toReturnTimes = times;

        List<ResponseReservationTime> result = reservationTimeController.getTimes();

        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).id()).isEqualTo(1L);
        Assertions.assertThat(result.get(0).startAt()).isEqualTo(LocalTime.of(10, 0));
    }
    
    private static class FakeReservationTimeService implements ReservationTimeService {

        LocalTime capturedStartAt;
        Long removedId;
        List<ReservationTime> toReturnTimes = List.of();
        ReservationTime toReturn;

        @Override
        public List<ReservationTime> getTimes() {
            return toReturnTimes;
        }

        @Override
        public ReservationTime createTime(LocalTime localTime) {
            this.capturedStartAt = localTime;
            return toReturn;
        }

        @Override
        public void removeTime(Long id) {
            this.removedId = id;
        }
    }
}
