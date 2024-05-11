package roomescape.ui.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.application.ReservationTimeService;
import roomescape.application.dto.response.ReservationTimeResponse;
import roomescape.fixture.ReservationTimeFixture;
import roomescape.support.ControllerTest;
import roomescape.support.SimpleMockMvc;
import roomescape.ui.controller.dto.ReservationTimeRequest;

class ReservationTimeControllerTest extends ControllerTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약_시간을_생성한다() throws Exception {
        ReservationTimeResponse response = ReservationTimeResponse.from(
                ReservationTimeFixture.DEFAULT_RESERVATION_TIME);
        when(reservationTimeService.register(any())).thenReturn(response);
        ReservationTimeRequest request = new ReservationTimeRequest(response.startAt());
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/times", content);

        result.andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(response.id()),
                        jsonPath("$.startAt").value(response.startAt().toString())
                )
                .andDo(print());
    }

    @Test
    void 예약_시간이_비어있으면_Bad_Request_상태를_반환한다() throws Exception {
        ReservationTimeRequest request = new ReservationTimeRequest(null);
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/times", content);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.fieldErrors[0].field").value("startAt"),
                        jsonPath("$.fieldErrors[0].rejectedValue").value(IsNull.nullValue()),
                        jsonPath("$.fieldErrors[0].reason").value("시작 시간은 필수입니다.")
                )
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1112", "14:89", "13-00"})
    void 예약_시간이_형식과_맞지_않으면_Bad_Request_상태를_반환한다(String startAt) throws Exception {
        String content = "{\"startAt\":" + startAt + "}";

        ResultActions result = SimpleMockMvc.post(mockMvc, "/times", content);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("잘못된 데이터 형식입니다.")
                )
                .andDo(print());
    }

    @Test
    void 전체_예약_시간을_조회한다() throws Exception {
        List<ReservationTimeResponse> reservationTimes = List.of(
                ReservationTimeResponse.from(ReservationTimeFixture.DEFAULT_RESERVATION_TIME),
                ReservationTimeResponse.from(ReservationTimeFixture.DEFAULT_RESERVATION_TIME),
                ReservationTimeResponse.from(ReservationTimeFixture.DEFAULT_RESERVATION_TIME)
        );
        when(reservationTimeService.findReservationTimes()).thenReturn(reservationTimes);

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
    void 예약_시간_아이디가_양수가_아니면_Bad_Request_상태를_반환한다() throws Exception {
        long invalidId = -1L;
        ResultActions result = SimpleMockMvc.delete(mockMvc, "/times/{id}", invalidId);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.violationErrors[0].field").value("id"),
                        jsonPath("$.violationErrors[0].rejectedValue").value(invalidId)
                )
                .andDo(print());
    }
}
