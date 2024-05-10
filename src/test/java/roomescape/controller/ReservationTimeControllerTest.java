package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.TestFixture.MIA_RESERVATION_DATE;
import static roomescape.TestFixture.MIA_RESERVATION_TIME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeSaveRequest;

import java.time.LocalDate;
import java.util.List;

class ReservationTimeControllerTest extends ControllerTest {

    @Test
    @DisplayName("예약 시간 POST 요청 시 상태코드 201을 반환한다.")
    void createReservationTime() throws Exception {
        // given
        final ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(MIA_RESERVATION_TIME);
        final ReservationTimeResponse expectedResponse = ReservationTimeResponse.from(new ReservationTime(1L, request.startAt()));

        given(reservationTimeService.create(any()))
                .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.startAt").value(MIA_RESERVATION_TIME.toString()));
    }

    @Test
    @DisplayName("잘못된 형식의 예약 시간 POST 요청 시 상태코드 400을 반환한다.")
    void createReservationTimeWithInvalidRequest() throws Exception {
        // given
        final ReservationTimeSaveRequest request = new ReservationTimeSaveRequest("15:03");

        given(reservationTimeService.create(any()))
                .willThrow(IllegalArgumentException.class);

        // when & then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("예약 시간 목록 GET 요청 시 상태코드 200을 반환한다.")
    void findReservationTimes() throws Exception {
        // given
        given(reservationTimeService.findAll())
                .willReturn(List.of(ReservationTimeResponse.from(new ReservationTime(MIA_RESERVATION_TIME))));

        // when & then
        mockMvc.perform(get("/times").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startAt").value(MIA_RESERVATION_TIME.toString()));
    }

    @Test
    @DisplayName("예약 시간 DELETE 요청 시 상태코드 204를 반환한다.")
    void deleteReservationTime() throws Exception {
        // given
        willDoNothing()
                .given(reservationTimeService)
                .delete(anyLong());

        // when & then
        mockMvc.perform(delete("/times/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 DELETE 요청 시 상태코드 404를 반환한다.")
    void deleteNotExistingReservationTime() throws Exception {
        // given
        willThrow(NotFoundException.class)
                .given(reservationTimeService)
                .delete(anyLong());

        // when & then
        mockMvc.perform(delete("/times/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("에약 가능 시간 목록 GET 요청 시 상태 코드를 200을 반환한다.")
    void findAllByDateAndThemeId() throws Exception {
        // given
        final LocalDate date = LocalDate.parse(MIA_RESERVATION_DATE);
        final Long themeId = 1L;
        given(reservationTimeService.findAvailableReservationTimes(date, themeId))
                .willReturn(List.of(AvailableReservationTimeResponse.of(new ReservationTime(MIA_RESERVATION_TIME), true)));

        // when & then
        mockMvc.perform(get("/times/available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("date", date.toString())
                        .param("themeId", themeId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startAt").value(MIA_RESERVATION_TIME))
                .andExpect(jsonPath("$[0].isReserved").value(true));
    }
}
