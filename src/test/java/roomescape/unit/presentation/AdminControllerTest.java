package roomescape.unit.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.auth.AuthorizationExtractor;
import roomescape.auth.Role;
import roomescape.dto.request.AdminReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.presentation.AdminController;
import roomescape.service.ReservationService;

@WebMvcTest(value = {AdminController.class})
@ImportAutoConfiguration
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private AuthorizationExtractor authorizationExtractor;

    @Test
    void 관리자가_예약을_생성한다() throws Exception {
        // given
        AdminReservationCreateRequest request = new AdminReservationCreateRequest(1L, LocalDate.of(2025, 1, 1), 1L, 1L);
        ReservationResponse response = new ReservationResponse(
                1L,
                "name1",
                LocalDate.of(2025, 1, 1),
                new ReservationTimeResponse(1L, LocalTime.of(9, 0)),
                "themeName1"
        );
        given(reservationService.createReservation(1L, 1L, 1L, LocalDate.of(2025, 1, 1))).willReturn(response);
        given(authorizationExtractor.extract(any(HttpServletRequest.class))).willReturn(Optional.of("token"));
        given(jwtTokenProvider.extractRole("token")).willReturn(Role.ADMIN);
        // when & then
        mockMvc.perform(post("/api/admin/reservations")
                        .cookie(new Cookie("token", "token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void 관리자가_예약을_조회한다() throws Exception {
        // given
        ReservationResponse reservationResponse = new ReservationResponse(
                1L,
                "name1",
                LocalDate.of(2025, 1, 1),
                new ReservationTimeResponse(1L, LocalTime.of(9, 0)),
                "themeName1"
        );
        List<ReservationResponse> response = List.of(reservationResponse);
        given(reservationService.findReservations(any())).willReturn(response);
        given(authorizationExtractor.extract(any(HttpServletRequest.class))).willReturn(Optional.of("token"));
        given(jwtTokenProvider.extractRole("token")).willReturn(Role.ADMIN);
        // when & then
        mockMvc.perform(get("/api/admin/reservations")
                        .cookie(new Cookie("token", "token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }
}