package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import roomescape.reservation.controller.dto.ReservationCreate;
import roomescape.reservation.service.ReservationService;
import roomescape.util.config.WebMvcTestConfig;
import roomescape.util.fixture.AuthFixture;

@Import(WebMvcTestConfig.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @DisplayName("예약 생성 요청이 올바르지 않으면 400을 반환한다")
    @MethodSource
    @ParameterizedTest
    void add_reservation_validate_test(LocalDate date, Long timeId, Long themeId, String errorFieldName,
                                       String errorMessage)
            throws Exception {
        // given
        String userToken = AuthFixture.createUserToken(authService);
        Long memberId = 3L;
        ReservationCreate reservationCreate = new ReservationCreate(date, timeId, themeId, memberId);

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie(LoginController.TOKEN_COOKIE_NAME, userToken))
                        .content(objectMapper.writeValueAsString(reservationCreate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$." + errorFieldName).value(errorMessage));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ReservationService reservationService() {
            return Mockito.mock(ReservationService.class);
        }

    }

    static Stream<Arguments> add_reservation_validate_test() {
        return Stream.of(
                Arguments.of(null, 1L, 1L, "date", "예약 날짜를 입력해 주세요."),
                Arguments.of(LocalDate.now(), null, 1L, "timeId", "시간을 선택해 주세요."),
                Arguments.of(LocalDate.now(), 1L, null, "themeId", "테마를 선택해 주세요.")
        );
    }

}
