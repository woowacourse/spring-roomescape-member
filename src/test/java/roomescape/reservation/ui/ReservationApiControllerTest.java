package roomescape.reservation.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import roomescape.auth.domain.Token;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.dto.MemberReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.dto.ThemeResponse;
import roomescape.time.dto.TimeResponse;

@WebMvcTest(ReservationApiController.class)
class ReservationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자는 예약을 등록할 수 있다")
    void test1() throws Exception {
        // given
        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);
        MemberReservationRequest request = new MemberReservationRequest(
                LocalDate.now().plusDays(1), 1L, 1L
        );

        MemberResponse memberResponse = MemberResponse.from(member);
        ThemeResponse theme = new ThemeResponse(1L, "테마1", "테마 설명", "테마 썸네일");
        TimeResponse time = new TimeResponse(1L, LocalTime.of(10, 0));
        ReservationResponse response = new ReservationResponse(1L, memberResponse, theme, LocalDate.now().plusDays(1), time);

        when(reservationService.addByUser(any(MemberReservationRequest.class), any(Member.class)))
                .thenReturn(response);

        when(authService.findMemberByToken(any(Token.class))).thenReturn(member);

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(new Cookie("token", "someValidToken")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.member.name").value("미미"))
                .andExpect(jsonPath("$.theme.name").value("테마1"))
                .andExpect(jsonPath("$.date").value(LocalDate.now().plusDays(1).toString()))
                .andExpect(jsonPath("$.time.startAt").value("10:00"));
    }

    @Test
    @DisplayName("전체 예약 목록을 조회할 수 있다")
    void test2() throws Exception {
        // given
        MemberResponse member = new MemberResponse(1L, "미미");
        ThemeResponse theme = new ThemeResponse(1L, "테마1", "테마 설명", "테마 썸네일");
        TimeResponse time = new TimeResponse(1L, LocalTime.of(10, 0));
        ReservationResponse response = new ReservationResponse(
                1L, member, theme, LocalDate.of(2025, 5, 20), time
        );

        when(reservationService.findAll()).thenReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].member.name").value("미미"))
                .andExpect(jsonPath("$[0].theme.name").value("테마1"));
    }

    @Test
    @DisplayName("예약을 삭제할 수 있다")
    void test3() throws Exception {
        // given
        doNothing().when(reservationService).deleteById(1L);

        // when & then
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());
    }
}
