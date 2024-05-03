package roomescape.presentation;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import roomescape.application.ReservationService;
import roomescape.application.dto.request.ReservationRequest;
import roomescape.application.dto.response.ReservationResponse;
import roomescape.application.dto.response.ReservationTimeResponse;
import roomescape.application.dto.response.ThemeResponse;
import roomescape.application.exception.DuplicatedEntityException;
import roomescape.domain.exception.EntityNotFoundException;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest extends ControllerTest {
    @MockBean
    private ReservationService reservationService;

    @DisplayName("저장된 모든 예약을 반환한다.")
    @Test
    void shouldReturnReservationResponsesWhenReservationsExist() throws Exception {
        ReservationResponse reservationResponse = new ReservationResponse(
                1L, "test", LocalDate.of(2024, 12, 25),
                new ReservationTimeResponse(1L, LocalTime.now()),
                new ThemeResponse(1L, "test", "test", "test"));
        String reservationResponsesJson = objectMapper.writeValueAsString(List.of(reservationResponse));

        given(reservationService.findAll())
                .willReturn(List.of(reservationResponse));

        mvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().json(reservationResponsesJson));
    }

    @DisplayName("저장된 예약이 없다면 빈 리스트를 반환한다.")
    @Test
    void shouldReturnEmptyListWhenReservationsIsEmpty() throws Exception {
        String reservationResponsesJson = objectMapper.writeValueAsString(List.of());

        given(reservationService.findAll())
                .willReturn(List.of());

        mvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().json(reservationResponsesJson));
    }

    @DisplayName("존재하지 않는 새로운 예약을 저장하면 201 Created 응답과 ReservationResponse가 반환된다.")
    @Test
    void shouldReturn201CreatedWithReservationResponseWhenNotExistReservationCreate() throws Exception {
        ReservationRequest reservationRequest = new ReservationRequest("test", LocalDate.now(), 1L, 1L);
        String reservationRequestJson = objectMapper.writeValueAsString(reservationRequest);

        ReservationResponse reservationResponse = new ReservationResponse(
                1L, "test", LocalDate.of(2024, 12, 25),
                new ReservationTimeResponse(1L, LocalTime.now()),
                new ThemeResponse(1L, "test", "test", "test"));
        String reservationResponseJson = objectMapper.writeValueAsString(reservationResponse);

        given(reservationService.create(reservationRequest))
                .willReturn(reservationResponse);

        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(reservationResponseJson));
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약을 생성하려고 하면 400 Bad Request 응답을 반환한다.")
    @Test
    void shouldReturn400BadRequestWhenNotFoundReservationTimeCreate() throws Exception {
        ReservationRequest reservationRequest = new ReservationRequest("test", LocalDate.now(), 1L, 1L);
        String reservationRequestJson = objectMapper.writeValueAsString(reservationRequest);

        given(reservationService.create(any(ReservationRequest.class)))
                .willThrow(new EntityNotFoundException("존재하지 않는 예약 시간 입니다."));

        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("존재하지 않는 예약 시간 입니다.")));
    }

    @DisplayName("이미 존재하는 예약을 생성하려고 하면 409 Conflict 응답을 반환한다.")
    @Test
    void shouldReturn400ConflictWhenAlreadyExistReservationCreate() throws Exception {
        ReservationRequest reservationRequest = new ReservationRequest("test", LocalDate.now(), 1L, 1L);
        String reservationRequestJson = objectMapper.writeValueAsString(reservationRequest);

        given(reservationService.create(reservationRequest))
                .willThrow(new DuplicatedEntityException("이미 존재하는 예약입니다."));

        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationRequestJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("이미 존재하는 예약입니다.")));
    }

    @DisplayName("존재하는 예약의 id로 삭제 요청을 하면 204 No Content 응답을 반환한다.")
    @Test
    void shouldReturn204SuccessWhenReservationIdExist() throws Exception {
        mvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());

        then(reservationService).should(times(1)).deleteById(any(Long.class));
    }

    @DisplayName("존재하지 않는 예약의 id로 삭제 요청을 하면 400 Bad Request 응답을 반환한다.")
    @Test
    void shouldReturn400BadRequestWhenReservationIdNotExist() throws Exception {
        doThrow(new EntityNotFoundException("존재하지 않는 예약입니다."))
                .when(reservationService).deleteById(1L);

        mvc.perform(delete("/reservations/1"))
                .andExpect(status().isBadRequest());

        then(reservationService).should(times(1)).deleteById(any(Long.class));
    }
}
