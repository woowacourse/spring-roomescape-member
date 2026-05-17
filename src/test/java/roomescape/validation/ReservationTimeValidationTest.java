package roomescape.validation;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.global.exception.GlobalExceptionHandler;
import roomescape.reservationtime.controller.ReservationTimeAdminController;
import roomescape.reservationtime.service.ReservationTimeService;

@WebMvcTest(ReservationTimeAdminController.class)
@Import(GlobalExceptionHandler.class)
class ReservationTimeValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 시간 생성 시 시간이 없으면 검증 예외")
    void create_whenStartAtNull_throws() throws Exception {
        String requestBody = """
                {
                  "startAt": null
                }
                """;

        mockMvc.perform(post("/admin/themes/1/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.validationErrors[0]").value("시간 지정은 필수입니다."));

        verifyNoInteractions(reservationTimeService);
    }
}
