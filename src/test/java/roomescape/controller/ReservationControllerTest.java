package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.format.DateTimeParseException;
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

    @DisplayName("지난 시점으로 예약하면 422 Unprocessable Entity를 응답한다")
    @Test
    void 예약_생성에서_ReservationByPastDateTimeException이_발생하면_422_UNPROCESSABLE_ENTITY를_응답한다() throws Exception {
        Mockito.when(reservationService.addReservation(Mockito.any(ReservationRequestDTO.class)))
                .thenThrow(ReservationByPastDateTimeException.class);

        String requestBody = """
                {
                    "name": "rudevico",
                    "date": "2026-05-01",
                    "timeId": 1,
                    "themeId": 1
                }
                """;
        mockMvc.perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @DisplayName("비어 있는 이름으로 예약하면 422 Unprocessable Entity를 응답한다")
    @Test
    void 예약_생성에서_EmptyNameException이_발생하면_422_UNPROCESSABLE_ENTITY를_응답한다() throws Exception {
        Mockito.when(reservationService.addReservation(Mockito.any(ReservationRequestDTO.class)))
                .thenThrow(EmptyNameException.class);

        String requestBody = """
                {
                    "name": "",
                    "date": "2026-05-30",
                    "timeId": 1,
                    "themeId": 1
                }
                """;
        mockMvc.perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isUnprocessableEntity());
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

    @DisplayName("올바르지 않은 날짜 형식으로 예약하면 422 Unprocessable Entity를 응답한다")
    @Test
    void 예약_생성에서_DateTimeParseException이_발생하면_422_UNPROCESSABLE_ENTITY를_응답한다() throws Exception {
        Mockito.when(reservationService.addReservation(Mockito.any()))
                .thenThrow(DateTimeParseException.class);

        String requestBody = """
                {
                    "name": "rudevico",
                    "date": "2026,05,20",
                    "timeId": 1,
                    "themeId": 1
                }
                """;
        mockMvc.perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isUnprocessableEntity());
    }
}
