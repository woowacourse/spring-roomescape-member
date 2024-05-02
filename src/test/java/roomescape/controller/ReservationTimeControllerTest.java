package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.IntStream;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.application.ReservationTimeService;
import roomescape.domain.time.ReservationTime;
import roomescape.dto.reservationtime.ReservationTimeRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.fixture.ReservationFixture;
import roomescape.support.ControllerTest;
import roomescape.support.SimpleMockMvc;

@DisplayName("예약 시간 컨트롤러")
class ReservationTimeControllerTest extends ControllerTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약_시간을_생성한다() throws Exception {
        ReservationTime reservationTime = ReservationFixture.reservationTime();
        when(reservationTimeService.register(any())).thenReturn(reservationTime);
        ReservationTimeRequest request = new ReservationTimeRequest(reservationTime.getStartAt());
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/times", content);

        result.andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(reservationTime.getId()),
                        jsonPath("$.startAt").value(reservationTime.getStartAt().toString())
                )
                .andDo(print());
    }

    @Test
    void 전체_예약_시간을_조회한다() throws Exception {
        List<ReservationTimeResponse> reservationTimes = IntStream.range(0, 3)
                .mapToObj(ReservationFixture::reservationTime)
                .map(time -> ReservationTimeResponse.from(time, false))
                .toList();
        when(reservationTimeService.getReservationTimes()).thenReturn(reservationTimes);

        ResultActions result = SimpleMockMvc.get(mockMvc, "/times");

        result.andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(reservationTimes.get(0).id()),
                        jsonPath("$[1].id").value(reservationTimes.get(1).id()),
                        jsonPath("$[2].id").value(reservationTimes.get(2).id())
                )
                .andDo(print());
    }

    @Test
    void 예약_시간을_삭제한다() throws Exception {
        long id = 1L;
        doNothing().when(reservationTimeService).delete(id);

        ResultActions result = SimpleMockMvc.delete(mockMvc, "/times/{id}", id);

        result.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void 예약_시간이_비어있으면_Bad_Request_상태를_반환한다() throws Exception {
        String content = "{}";

        ResultActions result = SimpleMockMvc.post(mockMvc, "/times", content);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.fieldErrors[0].field").value("startAt"),
                        jsonPath("$.fieldErrors[0].rejectedValue").value(IsNull.nullValue()),
                        jsonPath("$.fieldErrors[0].reason").value("시작 시간은 필수입니다.")
                )
                .andDo(print());
    }

    @Test
    void 예약_시간이_포맷에_맞지_않을_경우_Bad_Request_상태를_반환한다() throws Exception {
        String content = "{\"startAt\":\"1112\"}";

        ResultActions result = SimpleMockMvc.post(mockMvc, "/times", content);

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("잘못된 날짜 혹은 시간입니다."))
                .andDo(print());
    }

    @Test
    void 예약_시간이_올바르지_않을_경우_Bad_Request_상태를_반환한다() throws Exception {
        String content = "{\"startAt\":\"14:89\"}";

        ResultActions result = SimpleMockMvc.post(mockMvc, "/times", content);

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("잘못된 날짜 혹은 시간입니다."))
                .andDo(print());
    }
}
