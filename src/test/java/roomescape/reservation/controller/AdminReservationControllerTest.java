package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.global.config.WebMvcConfig;
import roomescape.reservation.controller.dto.ReservationCreate;
import roomescape.reservation.service.ReservationService;
import roomescape.util.config.WebMvcTestConfig;

@Import(WebMvcTestConfig.class)
@WebMvcTest(value = AdminReservationController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        WebMvcConfig.class
                }
        ))
class AdminReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("관리자 예약 생성 시 요청 값이 올바르지 않으면 예외가 발생한다")
    @MethodSource
    @ParameterizedTest
    void add_reservation_validate_test(LocalDate date, Long timeId, Long themeId, Long memberId, String errorFieldName,
                                       String errorMessage)
            throws Exception {
        // given
        ReservationCreate reservationCreate = new ReservationCreate(date, timeId, themeId, memberId);

        // when & then
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
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
                Arguments.of(null, 1L, 1L, 1L, "date", "예약 날짜를 입력해 주세요."),
                Arguments.of(LocalDate.now(), null, 1L, 1L, "timeId", "시간을 선택해 주세요."),
                Arguments.of(LocalDate.now(), 1L, null, 1L, "themeId", "테마를 선택해 주세요."),
                Arguments.of(LocalDate.now(), 1L, 1L, null, "memberId", "사용자를 선택해 주세요.")
        );
    }

}
