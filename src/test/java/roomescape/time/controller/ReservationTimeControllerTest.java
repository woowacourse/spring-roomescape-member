package roomescape.time.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import roomescape.auth.service.AuthService;
import roomescape.time.dto.ReservationTimeRequestDto;
import roomescape.time.service.ReservationTimeService;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationTimeService reservationTimeService;
    @MockBean
    private AuthService authService;

    @DisplayName("invalid한 save 요청이 들어오면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void invalidSave(String startAt) throws Exception {
        ReservationTimeRequestDto requestDto = new ReservationTimeRequestDto(startAt);
        mockMvc.perform(post("/times")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("invalid한 delete 요청이 들어오면 예외가 발생한다.")
    @Test
    void invalidDelete() throws Exception {
        mockMvc.perform(delete("/times/0"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("invalid한 가능 시간 조회 요청이 들어오면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("provideInvalidRequestDto")
    void invalidFindAvailableTime(String date, String themeId, String message) throws Exception {
        mockMvc.perform(get("/times/available")
                        .param("date", date)
                        .param("themeId", themeId))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidRequestDto() {
        return Stream.of(
                Arguments.of(" ", "1", "findAvailableTime.date: 날짜를 입력해야 합니다."),
                Arguments.of("", "1", "findAvailableTime.date: 날짜를 입력해야 합니다."),
                Arguments.of("hotea", "0", "findAvailableTime.themeId: 올바른 테마 ID를 입력해야 합니다.")
        );
    }
}
