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
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.ReservationTheme.ReservationTheme;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.service.RoomReservationService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@ActiveProfiles("web")
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomReservationService roomReservationService;

    private ReservationTime reservationTime;
    private ReservationTheme reservationTheme;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservationTime = new ReservationTime(1L, "10:00");
        reservationTheme = new ReservationTheme(1L, "테마1", "테마 설명", "image url");
        reservation = new Reservation(1L, "홍길동", "2026-05-06", reservationTime, reservationTheme);
    }

    @Test
    @DisplayName("예약 목록 조회 시 200과 바디를 반환한다")
    void getReservations() throws Exception {
        given(roomReservationService.getAllReservation())
                .willReturn(List.of(reservation));

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("홍길동"))
                .andExpect(jsonPath("$[0].date").value("2026-05-06"))
                .andExpect(jsonPath("$[0].time.id").value(1))
                .andExpect(jsonPath("$[0].time.startAt").value("10:00"))
                .andExpect(jsonPath("$[0].reservationTheme.id").value(1))
                .andExpect(jsonPath("$[0].reservationTheme.name").value("테마1"));
    }

    @Test
    @DisplayName("예약 추가 시 200과 바디를 반환한다")
    void addReservation() throws Exception {
        given(roomReservationService.addReservation(any()))
                .willReturn(reservation);

        String requestBody = """
            {
                "name": "홍길동",
                "date": "2026-05-06",
                "timeId": 1,
                "themeId": 1
            }
        """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.date").value("2026-05-06"))
                .andExpect(jsonPath("$.time.startAt").value("10:00"))
                .andExpect(jsonPath("$.reservationTheme.name").value("테마1"));
    }

    @Test
    @DisplayName("예약 삭제 시 204를 반환한다")
    void deleteReservation() throws Exception {
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("이름으로 예약 조회 시 200과 바디를 반환한다")
    void getReservationByName() throws Exception {
        given(roomReservationService.getAllReservationByName("홍길동"))
                .willReturn(List.of(reservation));

        mockMvc.perform(get("/reservations")
                        .param("name", "홍길동"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("홍길동"));
    }

    @Test
    @DisplayName("필수 값 없이 예약 추가 시 400을 반환한다")
    void addReservationWithInvalidRequest() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}