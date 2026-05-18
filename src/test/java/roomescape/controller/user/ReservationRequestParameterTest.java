package roomescape.controller.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationRequestParameterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 조회 시 이름 쿼리 파라미터가 없으면 에러 응답을 반환한다.")
    void failFind_WhenNameParameterIsMissing() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.path").value("/reservations"));
    }

    @Test
    @DisplayName("예약 취소 시 예약 id 형식이 올바르지 않으면 에러 응답을 반환한다.")
    void failDelete_WhenReservationIdIsInvalid() throws Exception {
        mockMvc.perform(delete("/reservations/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.path").value("/reservations/abc"));
    }

    @Test
    @DisplayName("지원하지 않는 Content-Type으로 예약을 생성하면 에러 응답을 반환한다.")
    void failCreate_WhenContentTypeIsUnsupported() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("invalid"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.code").value("UNSUPPORTED_MEDIA_TYPE"))
                .andExpect(jsonPath("$.path").value("/reservations"));
    }

    @Test
    @DisplayName("지원하지 않는 HTTP 메서드로 요청하면 에러 응답을 반환한다.")
    void failRequest_WhenMethodIsUnsupported() throws Exception {
        mockMvc.perform(delete("/reservations"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.path").value("/reservations"));
    }

    @Test
    @DisplayName("예약 변경 성공 시 변경된 예약 정보를 반환한다.")
    void changeReservationDateTime() throws Exception {
        LocalDate changedDate = LocalDate.of(2026, 5, 16);
        Reservation changedReservation = new Reservation(
                1L,
                "브라운",
                changedDate,
                new ReservationTime(2L, LocalTime.of(11, 0)),
                new Theme(1L, "우주 정거장", "설명", "https://example.com/1.jpg")
        );

        given(reservationService.changeReservationDateTime(eq(1L), any()))
                .willReturn(changedReservation);

        mockMvc.perform(patch("/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "2026-05-16",
                                  "timeId": 2
                                }
                                """))
                .andExpect(status().isOk());
    }
}
