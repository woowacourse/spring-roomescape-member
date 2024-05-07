package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReservationService reservationService;

    @DisplayName("invalid한 save 요청이 들어오면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("provideInvalidRequestDto")
    void invalidSave(ReservationRequestDto reservationRequestDto, String message) throws Exception {
        mockMvc.perform(post("/reservations")
                       .content(objectMapper.writeValueAsString(reservationRequestDto))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().string(message));
    }

    private static Stream<Arguments> provideInvalidRequestDto() {
        return Stream.of(
                Arguments.of(new ReservationRequestDto(null, "2024-04-01", 1, 1), "field : name" + System.lineSeparator() + "message : 예약자의 이름을 입력해야 합니다."),
                Arguments.of(new ReservationRequestDto("hotea", null, 1, 1), "field : date" + System.lineSeparator() + "message : 예약날짜를 입력해야 합니다."),
                Arguments.of(new ReservationRequestDto("hotea", "2024-04-01", 0, 1), "field : timeId" + System.lineSeparator() + "message : 올바른 예약 시간 ID를 입력해야 합니다."),
                Arguments.of(new ReservationRequestDto("hotea", "2024-04-01", 1, 0), "field : themeId" + System.lineSeparator() + "message : 올바른 예약 테마 ID를 입력해야 합니다.")
        );
    }

    @DisplayName("invalid한 delete 요청이 들어오면 예외가 발생한다.")
    @Test
    void invalidDelete() throws Exception {
        mockMvc.perform(delete("/reservations/0"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("delete.id: 올바른 예약 ID를 입력해야 합니다."));

    }
}
