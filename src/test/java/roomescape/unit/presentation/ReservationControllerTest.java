package roomescape.unit.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.auth.AuthorizationExtractor;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.presentation.ReservationController;
import roomescape.service.ReservationService;

@WebMvcTest(value = {ReservationController.class, AuthorizationExtractor.class})
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private JwtTokenProvider tokenProvider;

    @Test
    void 사용자가_예약을_생성한다() throws Exception {
        // given
        ReservationCreateRequest request = new ReservationCreateRequest(LocalDate.of(2025, 1, 1), 1L, 1L);
        ReservationResponse response = new ReservationResponse(1L, "memberName1", LocalDate.of(2025, 1, 1),
                new ReservationTimeResponse(1L, LocalTime.of(9, 0)), "themeName1");
        given(reservationService.createReservation(1L, request.timeId(), request.themeId(), request.date())).willReturn(
                response);
        given(tokenProvider.extractSubject("accessToken")).willReturn("1");
        // when & then
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(new Cookie("token", "accessToken")))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void 예약을_삭제하는데_성공한다() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/reservations/{reservationId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}