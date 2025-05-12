package roomescape.reservation.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.auth.application.AuthService;
import roomescape.auth.domain.Payload;
import roomescape.auth.domain.Token;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.dto.ThemeResponse;
import roomescape.time.dto.TimeResponse;

@WebMvcTest(AdminReservationApiController.class)
class AdminReservationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("관리자는 예약 검색 필터로 예약 목록을 조회할 수 있다")
    void test1() throws Exception {
        // given
        MemberResponse admin = new MemberResponse(1L, "미미");
        ThemeResponse theme = new ThemeResponse(1L, "테마1", "테마 설명", "테마 썸네일");
        TimeResponse time = new TimeResponse(1L, LocalTime.of(10, 0));
        ReservationResponse reservation = new ReservationResponse(
                1L, admin, theme, LocalDate.of(2025, 5, 12), time
        );

        Payload adminPayload = mock(Payload.class);
        when(authService.getPayload(any(Token.class))).thenReturn(adminPayload);
        when(adminPayload.isAuthorizedFor(Role.ADMIN)).thenReturn(true);

        when(reservationService.findBySearchFilter(any(), any(), any(), any()))
                .thenReturn(List.of(reservation));

        // when & then
        mockMvc.perform(get("/admin/reservations")
                        .cookie(new Cookie("token", "valid-token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("관리자는 예약을 등록할 수 있다")
    void test2() throws Exception {
        // given
        AdminReservationRequest validRequest = new AdminReservationRequest(
                LocalDate.of(2025, 5, 12),
                1L,
                1L,
                1L
        );

        MemberResponse admin = new MemberResponse(1L, "미미");
        ThemeResponse theme = new ThemeResponse(1L, "테마1", "테마 설명", "테마 썸네일");
        TimeResponse time = new TimeResponse(1L, LocalTime.of(10, 0));
        ReservationResponse response = new ReservationResponse(
                1L, admin, theme, LocalDate.of(2025, 5, 12), time
        );

        Payload adminPayload = mock(Payload.class);
        when(authService.getPayload(any(Token.class))).thenReturn(adminPayload);
        when(adminPayload.isAuthorizedFor(Role.ADMIN)).thenReturn(true);

        when(reservationService.addByAdmin(any(AdminReservationRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/admin/reservations")
                        .cookie(new Cookie("token", "valid-token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.member.name").value("미미"))
                .andExpect(jsonPath("$.theme.name").value("테마1"))
                .andExpect(jsonPath("$.date").value("2025-05-12"))
                .andExpect(jsonPath("$.time.startAt").value("10:00"));
    }
}
