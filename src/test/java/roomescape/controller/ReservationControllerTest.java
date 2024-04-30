package roomescape.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.exception.NotFoundException;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.TestFixture.*;

class ReservationControllerTest extends ControllerTest {

    @Test
    @DisplayName("예약 목록 GET 요청 시 상태코드 200을 반환한다.")
    void findReservations() throws Exception {
        // given
        ReservationTime expectedTime = new ReservationTime(1L, MIA_RESERVATION_TIME);
        Reservation expectedReservation = MIA_RESERVATION(expectedTime);

        BDDMockito.given(reservationService.findAll())
                .willReturn(List.of(ReservationResponse.from(expectedReservation)));

        // when & then
        mockMvc.perform(get("/reservations").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(USER_MIA))
                .andExpect(jsonPath("$[0].time.id").value(1L))
                .andExpect(jsonPath("$[0].time.startAt").value(MIA_RESERVATION_TIME.toString()))
                .andExpect(jsonPath("$[0].date").value(MIA_RESERVATION_DATE.toString()));
    }

    @Test
    @DisplayName("예약 POST 요청 시 상태코드 201을 반환한다.")
    void createReservation() throws Exception {
        // given
        ReservationSaveRequest request = new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, 1L);
        ReservationTime expectedTime = new ReservationTime(1L, MIA_RESERVATION_TIME);
        ReservationResponse expectedResponse = ReservationResponse.of(MIA_RESERVATION(), expectedTime);

        BDDMockito.given(reservationService.create(any()))
                .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(USER_MIA))
                .andExpect(jsonPath("$.time.id").value(1L))
                .andExpect(jsonPath("$.time.startAt").value(MIA_RESERVATION_TIME.toString()))
                .andExpect(jsonPath("$.date").value(MIA_RESERVATION_DATE.toString()));
    }

    @ParameterizedTest
    @MethodSource(value = "invalidPostRequests")
    @DisplayName("잘못된 형식의 예약 POST 요청 시 상태코드 400을 반환한다.")
    void createReservationWithInvalidRequest(ReservationSaveRequest request) throws Exception {
        // given
        BDDMockito.given(reservationService.create(any()))
                .willThrow(IllegalArgumentException.class);

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    private static Stream<ReservationSaveRequest> invalidPostRequests() {
        return Stream.of(
                new ReservationSaveRequest(null, MIA_RESERVATION_DATE, 1L),
                new ReservationSaveRequest(USER_MIA, null, 1L)
        );
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간의 예약 POST 요청 시 상태코드 404를 반환한다.")
    void createReservationWithNotExistTime() throws Exception {
        // given
        Long notExistingId = 1L;
        ReservationSaveRequest request = new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, notExistingId);

        BDDMockito.willThrow(NotFoundException.class)
                .given(reservationService)
                .create(any());

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("예약 DELETE 요청 시 상태코드 204를 반환한다.")
    void deleteReservation() throws Exception {
        // given
        BDDMockito.willDoNothing()
                .given(reservationService)
                .delete(anyLong());

        // when & then
        mockMvc.perform(delete("/reservations/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 예약 DELETE 요청 시 상태코드 404를 반환한다.")
    void deleteNotExistingReservation() throws Exception {
        // given
        BDDMockito.willThrow(NotFoundException.class)
                .given(reservationService)
                .delete(anyLong());

        // when & then
        mockMvc.perform(delete("/reservations/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
