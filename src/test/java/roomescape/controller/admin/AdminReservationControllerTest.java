package roomescape.controller.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
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
}
