package roomescape.reservation.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import roomescape.common.Role;
import roomescape.config.RoleCheckInterceptor;
import roomescape.config.WebMvcControllerTestConfig;
import roomescape.config.security.JwtTokenProvider;
import roomescape.exception.ExceptionPageController;
import roomescape.member.dto.LoginMember;

@WebMvcTest(ReservationPageController.class)
@Import({WebMvcControllerTestConfig.class, ExceptionPageController.class})
class ReservationPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ReservationPageController())
                .setControllerAdvice(new ExceptionPageController())
                .addInterceptors(new RoleCheckInterceptor(jwtTokenProvider))
                .build();
    }

    @Test
    @DisplayName("/reservation 를 요청하면 reservation.html 를 반환한다.")
    void requestUserReservation() throws Exception {
        LoginMember loginMember = new LoginMember(1L, Role.MEMBER, "어드민", "admin@email.com");
        doReturn(loginMember).when(jwtTokenProvider)
                .getMember(anyString());

        mockMvc.perform(get("/reservation")
                        .cookie(new Cookie("token", "cookieValue")))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"));
    }

    @Test
    @DisplayName("/admin/reservation 를 요청하면 admin/reservation-new.html 를 반환한다.")
    void requestAdminReservation() throws Exception {
        LoginMember loginMember = new LoginMember(1L, Role.ADMIN, "어드민", "admin@email.com");
        doReturn(loginMember).when(jwtTokenProvider)
                .getMember(anyString());

        mockMvc.perform(get("/admin/reservation")
                        .cookie(new Cookie("token", "cookieValue")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reservation-new"));
    }

    @Test
    @DisplayName("일반 사용자가 접근시에 403과 에러 페이지를 반환한다.")
    void AdminPathAccessDenied() throws Exception {
        LoginMember loginMember = new LoginMember(1L, Role.MEMBER, "카키", "kaki@email.com");
        doReturn(loginMember).when(jwtTokenProvider)
                .getMember(anyString());

        mockMvc.perform(get("/admin/reservation")
                        .cookie(new Cookie("token", "cookieValue")))
                .andExpect(status().isForbidden())
                .andExpect(view().name("error/403"));
    }
}
