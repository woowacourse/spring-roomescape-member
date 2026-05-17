package roomescape.validation;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.global.auth.resolver.UserArgumentResolver;
import roomescape.global.config.WebConfig;
import roomescape.global.exception.GlobalExceptionHandler;
import roomescape.reservation.controller.ReservationController;
import roomescape.reservation.service.ReservationService;

@WebMvcTest(ReservationController.class)
@Import({
        GlobalExceptionHandler.class,
        WebConfig.class,
        UserArgumentResolver.class
})
class ReservationValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 생성 시 이름이 비어 있으면 검증 예외")
    void create_whenNameBlank_throws() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "date": "2026-05-15",
                  "timeId": 1
                }
                """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.validationErrors[0]").value("이름은 비어 있을 수 없습니다."));
        verifyNoInteractions(reservationService);
    }

    @Test
    @DisplayName("예약 생성 시 날짜가 없으면 검증 예외")
    void create_whenDateNull_throws() throws Exception {
        String requestBody = """
                {
                  "name": "브라운",
                  "date": null,
                  "timeId": 1
                }
                """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.validationErrors[0]").value("예약 날짜는 필수입니다."));
        verifyNoInteractions(reservationService);
    }

    @Test
    @DisplayName("예약 생성 시 시간 ID가 없으면 검증 예외")
    void create_whenTimeIdNull_throws() throws Exception {
        String requestBody = """
                {
                  "name": "브라운",
                  "date": "2026-05-15",
                  "timeId": null
                }
                """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.validationErrors[0]").value("시간 지정은 필수입니다."));
        verifyNoInteractions(reservationService);
    }

    @Test
    @DisplayName("예약 수정 시 날짜가 없으면 검증 예외")
    void update_whenDateNull_throws() throws Exception {
        String requestBody = """
                {
                  "date": null,
                  "timeId": 1
                }
                """;

        mockMvc.perform(patch("/reservations/1")
                        .header("Authorization", authorizationHeader("브라운"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.validationErrors[0]").value("예약 날짜는 필수입니다."));
        verifyNoInteractions(reservationService);
    }

    @Test
    @DisplayName("예약 수정 시 시간 ID가 없으면 검증 예외")
    void update_whenTimeIdNull_throws() throws Exception {
        String requestBody = """
                {
                  "date": "2026-05-15",
                  "timeId": null
                }
                """;

        mockMvc.perform(patch("/reservations/1")
                        .header("Authorization", authorizationHeader("브라운"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.validationErrors[0]").value("시간 지정은 필수입니다."));
        verifyNoInteractions(reservationService);
    }

    private String authorizationHeader(final String name) {
        return "Bearer " + URLEncoder.encode(name, StandardCharsets.UTF_8);
    }

}
