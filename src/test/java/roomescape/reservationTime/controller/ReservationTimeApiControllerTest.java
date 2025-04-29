package roomescape.reservationTime.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservationTime.service.ReservationTimeService;

@WebMvcTest(ReservationTimeApiController.class)
class ReservationTimeApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    @DisplayName("시간이 빈 값일 경우 예외가 발생한다")
    @Test
    void test1() throws Exception {
        String requestBody = """
                {
                    "startAt": ""
                }
                """;

        mockMvc.perform(post("/times")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("시간이 형식에 맞지 않을 경우 예외가 발생한다")
    @Test
    void test2() throws Exception {
        String requestBody = """
                {
                    "startAt": "2025/04/30"
                }
                """;

        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

}
