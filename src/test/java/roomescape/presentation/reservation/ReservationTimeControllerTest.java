package roomescape.presentation.reservation;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import roomescape.application.reservation.ReservationTimeService;
import roomescape.application.reservation.dto.request.ReservationTimeRequest;
import roomescape.application.reservation.dto.response.ReservationTimeResponse;
import roomescape.auth.TokenManager;
import roomescape.domain.role.RoleRepository;
import roomescape.presentation.ControllerTest;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerTest extends ControllerTest {
    @MockBean
    private ReservationTimeService reservationTimeService;

    @MockBean
    private TokenManager tokenManager;

    @MockBean
    private RoleRepository roleRepository;

    @DisplayName("예약 시간 저장을 요청하면, 해당 예약 시간의 저장 id와 시간 201 Created 응답으로 반환한다.")
    @Test
    void shouldReturnReservationTimeResponseWith201CreatedWhenCreateReservationTime() throws Exception {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(1L, LocalTime.of(10, 0));

        String reservationTimeRequestJson = objectMapper.writeValueAsString(reservationTimeRequest);

        given(reservationTimeService.create(reservationTimeRequest))
                .willReturn(reservationTimeResponse);

        mvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationTimeRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(reservationTimeResponse)));
    }

    @DisplayName("예약 시간 저장 요청 시, 올바르지 않은 예약 시간이면 400 Bad Request 응답을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"25:00", "10:60"})
    void shouldReturn400BadRequestWhenCreateInvalidReservationTime(String time) throws Exception {
        String reservationTimeRequestJson = "{\"startAt\": \"" + time + "\"}";
        mvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationTimeRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("값을 변환하는 중 오류가 발생했습니다.")));
    }

    @DisplayName("예약 시간을 조회하면, 200 OK 응답으로 저장된 예약 시간을 모두 반환한다.")
    @Test
    void shouldReturnReservationTimeResponsesWith200OkWhenFindAllReservationTimes() throws Exception {
        List<ReservationTimeResponse> reservationTimeResponses = List.of(
                new ReservationTimeResponse(1L, LocalTime.of(10, 0)),
                new ReservationTimeResponse(2L, LocalTime.of(11, 0)),
                new ReservationTimeResponse(3L, LocalTime.of(12, 0))
        );

        String reservationTimeResponsesJson = objectMapper.writeValueAsString(reservationTimeResponses);

        given(reservationTimeService.findAll())
                .willReturn(reservationTimeResponses);

        mvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(content().json(reservationTimeResponsesJson));
    }

    @DisplayName("예약 id로 삭제 요청을 하면, 204 No Content 응답으로 저장되어있는 예약을 삭제한다.")
    @Test
    void shouldReturn204NoContentWithoutResponseWhenDeleteReservationTime() throws Exception {
        mvc.perform(delete("/times/1"))
                .andExpect(status().isNoContent());

        then(reservationTimeService).should(times(1)).deleteById(1L);
    }
}
