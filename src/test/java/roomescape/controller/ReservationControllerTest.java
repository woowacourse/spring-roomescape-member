package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.dto.ReservationRequestDTO;
import roomescape.exception.DuplicatedReservationException;
import roomescape.exception.EmptyNameException;
import roomescape.exception.ReservationByPastDateTimeException;
import roomescape.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ReservationService reservationService;

    @DisplayName("잘못된 예약 인자값에 대해 400 상태 코드를 반환한다")
    @Test
    void 예약_생성에서_ReservationByPastDateTimeException이_발생하면_400_BAD_REQUEST를_응답한다() throws Exception {
        Mockito.when(reservationService.addReservation(Mockito.any(ReservationRequestDTO.class)))
                .thenThrow(ReservationByPastDateTimeException.class);

        mockMvc.perform(post("/reservations"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("비어 있는 이름으로 예약하면 400 상태 코드를 반환한다")
    @Test
    void 예약_생성에서_EmptyNameException이_발생하면_400_BAD_REQUEST를_응답한다() throws Exception {
        // TODO 이거 잘 변환되어서 400 뜨는게 아니고, request body 필요한데 전달 안 해서 400 뜨는 거임
        Mockito.when(reservationService.addReservation(Mockito.any(ReservationRequestDTO.class)))
                .thenThrow(EmptyNameException.class);

        mockMvc.perform(post("/reservations"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("같은 날짜/시간/테마로 중복 예약하면 409 Conflict를 응답한다")
    @Test
    void 예약_생성에서_DuplicatedReservationException이_발생하면_409_CONFLICT를_응답한다() throws Exception {
        Mockito.when(reservationService.addReservation(Mockito.any()))
                .thenThrow(DuplicatedReservationException.class);

        String requestBody = """
                {
                    "name": "rudevico",
                    "date": "2026-05-20",
                    "timeId": 1,
                    "themeId": 1
                }
                """;
        mockMvc.perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isConflict());
    }
}
