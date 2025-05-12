package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.auth.controller.LoginController;
import roomescape.auth.service.AuthService;
import roomescape.reservation.controller.dto.ReservationTimeRequest;
import roomescape.reservation.service.ReservationTimeService;
import roomescape.util.config.WebMvcTestConfig;
import roomescape.util.fixture.AuthFixture;

@Import(WebMvcTestConfig.class)
@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @DisplayName("시간 생성 요청이 올바르지 않으면 400을 반환한다")
    @Test
    void add_time_validate_test() throws Exception {
        // given
        String adminToken = AuthFixture.createAdminToken(authService);
        ReservationTimeRequest request = new ReservationTimeRequest(null);

        // when & then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie(LoginController.TOKEN_COOKIE_NAME, adminToken))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.startAt").value("시작 시간을 입력해 주세요."));
    }

    @TestConfiguration
    static class MockConfig {

        @Bean
        public ReservationTimeService reservationTimeService() {
            return Mockito.mock(ReservationTimeService.class);
        }

    }

}
