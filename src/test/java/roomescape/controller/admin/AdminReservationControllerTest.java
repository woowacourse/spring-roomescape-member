package roomescape.controller.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.service.ReservationService;

@WebMvcTest(AdminReservationController.class)
class AdminReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    @DisplayName("예약자 이름이 공백인 경우 예약 생성에 실패한다.")
    void failCreate_WhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "date": "2026-05-14",
                                  "timeId": 1,
                                  "themeId": 1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("예약 날짜가 null인 경우 예약 생성에 실패한다.")
    void failCreate_WhenDateIsNull() throws Exception {
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "브라운",
                                  "date": null,
                                  "timeId": 1,
                                  "themeId": 1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("예약 날짜가 yyyy-MM-dd 형식이 아닌 경우 예약 생성에 실패한다.")
    void failCreate_WhenDateFormatIsInvalid() throws Exception {
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "브라운",
                                  "date": "2026/05/14",
                                  "timeId": 1,
                                  "themeId": 1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("예약 시간이 null인 경우 예약 생성에 실패한다.")
    void failCreate_WhenTimeIdIsNull() throws Exception {
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "브라운",
                                  "date": "2026-05-14",
                                  "timeId": null,
                                  "themeId": 1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("테마가 null인 경우 예약 생성에 실패한다.")
    void failCreate_WhenThemeIdIsNull() throws Exception {
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "브라운",
                                  "date": "2026-05-14",
                                  "timeId": 1,
                                  "themeId": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("중복 예약인 경우 의도된 에러 응답을 반환한다.")
    void failCreate_WhenReservationDuplicated() throws Exception {
        given(reservationService.saveReservation(any()))
                .willThrow(new RoomescapeException(ErrorCode.RESERVATION_DUPLICATED));

        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "브라운",
                                  "date": "2026-05-14",
                                  "timeId": 1,
                                  "themeId": 1
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("존재하지 않는 예약 삭제 시 에러 응답을 반환한다.")
    void failDelete_WhenReservationNotFound() throws Exception {
        willThrow(new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND))
                .given(reservationService)
                .deleteById(1L);

        mockMvc.perform(delete("/admin/reservations/1"))
                .andExpect(status().isNotFound());
    }
}
