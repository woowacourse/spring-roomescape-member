package roomescape.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.service.ReservationTimeService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationTimeController.class)
@ActiveProfiles("web")
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    private ReservationTime reservationTime;

    @BeforeEach
    void setUp() {
        reservationTime = new ReservationTime(1L, "10:00");
    }

    @Test
    @DisplayName("시간 목록 조회 시 200과 바디를 반환한다")
    void getReservationTimes() throws Exception {
        given(reservationTimeService.getAllReservationTime())
                .willReturn(List.of(reservationTime));

        mockMvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].startAt").value("10:00"));
    }

    @Test
    @DisplayName("시간 추가 시 200과 바디를 반환한다")
    void addReservationTime() throws Exception {
        given(reservationTimeService.addReservationTime(any()))
                .willReturn(reservationTime);

        String requestBody = """
            {
                "startAt": "10:00"
            }
        """;

        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startAt").value("10:00"));
    }

    @Test
    @DisplayName("잘못된 시간 형식으로 추가 시 400을 반환한다")
    void addReservationTimeWithInvalidFormat() throws Exception {
        String requestBody = """
            {
                "startAt": "1000"
            }
        """;

        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("시간 값 없이 추가 시 400을 반환한다")
    void addReservationTimeWithBlank() throws Exception {
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("시간 삭제 시 204를 반환한다")
    void deleteReservationTime() throws Exception {
        mockMvc.perform(delete("/times/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("날짜와 테마로 예약 가능 시간 조회 시 200과 바디를 반환한다")
    void getReservationTimeWithAvailable() throws Exception {
        List<ReservationTimeWithAvailable> timesWithAvailable = List.of(
                new ReservationTimeWithAvailable(1L, "10:00", true),
                new ReservationTimeWithAvailable(2L, "12:00", false)
        );
        given(reservationTimeService.getAvailableReservationTimeByDateAndTheme(any()))
                .willReturn(timesWithAvailable);

        mockMvc.perform(get("/times/availability")
                        .param("date", "2026-05-06")
                        .param("themeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].startAt").value("10:00"))
                .andExpect(jsonPath("$[0].isAvailable").value(true))
                .andExpect(jsonPath("$[1].isAvailable").value(false));
    }

    @Test
    @DisplayName("잘못된 날짜 형식으로 예약 가능 시간 조회 시 400을 반환한다")
    void getReservationTimeWithInvalidDate() throws Exception {
        mockMvc.perform(get("/times/availability")
                        .param("date", "20260506")
                        .param("themeId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유효하지 않은 테마 ID로 예약 가능 시간 조회 시 400을 반환한다")
    void getReservationTimeWithInvalidThemeId() throws Exception {
        mockMvc.perform(get("/times/availability")
                        .param("date", "2026-05-06")
                        .param("themeId", "0"))
                .andExpect(status().isBadRequest());
    }
}
