package roomescape.ui.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.application.ReservationService;
import roomescape.application.dto.response.ReservationResponse;
import roomescape.fixture.ReservationFixture;
import roomescape.support.ControllerTest;
import roomescape.support.SimpleMockMvc;
import roomescape.ui.controller.dto.ReservationRequest;

class ReservationControllerTest extends ControllerTest {
    @Autowired
    private ReservationService reservationService;

    @Test
    void 예약을_생성한다() throws Exception {
        ReservationResponse response = ReservationResponse.from(ReservationFixture.DEFAULT_RESERVATION);
        when(reservationService.reserve(any())).thenReturn(response);
        ReservationRequest request = new ReservationRequest(response.date(), response.time().id(),
                response.theme().id());
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(response.id()),
                        jsonPath("$.date").value(response.date().toString()),
                        jsonPath("$.time.id").value(response.time().id()),
                        jsonPath("$.time.startAt").value(response.time().startAt().toString())
                )
                .andDo(print());
    }

    @Test
    void 예약날짜가_비어있으면_Bad_Request_상태를_반환한다() throws Exception {
        ReservationRequest request = new ReservationRequest(null, 1L, 1L);
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.fieldErrors[0].field").value("date"),
                        jsonPath("$.fieldErrors[0].rejectedValue").value(IsNull.nullValue()),
                        jsonPath("$.fieldErrors[0].reason").value("예약날짜는 필수입니다.")
                )
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024_04_30", "2024-04-70"})
    void 예약날짜가_형식과_맞지_않으면_Bad_Request_상태를_반환한다(String date) throws Exception {
        String content = "{\"date\":" + date + ", \"timeId\":1, \"themeId\":1}";

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("잘못된 데이터 형식입니다.")
                )
                .andDo(print());
    }

    @Test
    void 시간_아이디가_비어있으면_Bad_Request_상태를_반환한다() throws Exception {
        ReservationRequest request = new ReservationRequest(LocalDate.parse("2024-04-30"), null, 1L);
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.fieldErrors[0].field").value("timeId"),
                        jsonPath("$.fieldErrors[0].rejectedValue").value(IsNull.nullValue()),
                        jsonPath("$.fieldErrors[0].reason").value("시간 아이디는 필수입니다.")
                )
                .andDo(print());
    }

    @Test
    void 시간_아이디가_양수가_아니면_Bad_Request_상태를_반환한다() throws Exception {
        ReservationRequest request = new ReservationRequest(LocalDate.parse("2024-04-30"), -1L, 1L);
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.fieldErrors[0].field").value("timeId"),
                        jsonPath("$.fieldErrors[0].rejectedValue").value(-1),
                        jsonPath("$.fieldErrors[0].reason").value("시간 아이디는 양수여야 합니다.")
                )
                .andDo(print());
    }

    @Test
    void 테마_아이디가_비어있으면_Bad_Request_상태를_반환한다() throws Exception {
        ReservationRequest request = new ReservationRequest(LocalDate.parse("2024-04-30"), 1L, null);
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.fieldErrors[0].field").value("themeId"),
                        jsonPath("$.fieldErrors[0].rejectedValue").value(IsNull.nullValue()),
                        jsonPath("$.fieldErrors[0].reason").value("테마 아이디는 필수입니다.")
                )
                .andDo(print());
    }

    @Test
    void 테마_아이디가_양수가_아니면_Bad_Request_상태를_반환한다() throws Exception {
        ReservationRequest request = new ReservationRequest(LocalDate.parse("2024-04-30"), 1L, -1L);
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.fieldErrors[0].field").value("themeId"),
                        jsonPath("$.fieldErrors[0].rejectedValue").value(-1),
                        jsonPath("$.fieldErrors[0].reason").value("테마 아이디는 양수여야 합니다.")
                )
                .andDo(print());
    }

    @Test
    void 전체_예약을_조회한다() throws Exception {
        List<ReservationResponse> reservations = List.of(
                ReservationResponse.from(ReservationFixture.DEFAULT_RESERVATION),
                ReservationResponse.from(ReservationFixture.DEFAULT_RESERVATION),
                ReservationResponse.from(ReservationFixture.DEFAULT_RESERVATION)
        );
        when(reservationService.findReservations()).thenReturn(reservations);

        ResultActions result = SimpleMockMvc.get(mockMvc, "/reservations");

        result.andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(reservations.get(0).id()),
                        jsonPath("$[1].id").value(reservations.get(1).id()),
                        jsonPath("$[2].id").value(reservations.get(2).id())
                )
                .andDo(print());
    }

    @Test
    void 예약을_취소한다() throws Exception {
        long id = 1L;
        doNothing().when(reservationService).cancel(id);

        ResultActions result = SimpleMockMvc.delete(mockMvc, "/reservations/{id}", id);

        result.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void 예약_아이디가_양수가_아니면_Bad_Request_상태를_반환한다() throws Exception {
        long invalidId = -1L;

        ResultActions result = SimpleMockMvc.delete(mockMvc, "/reservations/{id}", invalidId);

        result.andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.violationErrors[0].field").value("id"),
                        jsonPath("$.violationErrors[0].rejectedValue").value(invalidId)
                )
                .andDo(print());
    }
}
