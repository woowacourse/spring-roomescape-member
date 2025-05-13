package roomescape.domain.reservation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.domain.auth.config.JwtConfig;
import roomescape.domain.auth.config.JwtProperties;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Password;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.service.AuthService;
import roomescape.domain.auth.service.JwtManager;
import roomescape.domain.reservation.dto.reservation.ReservationResponse;
import roomescape.domain.reservation.dto.reservationtime.ReservationTimeResponse;
import roomescape.domain.reservation.dto.theme.ThemeResponse;
import roomescape.domain.reservation.service.ReservationService;

@WebMvcTest(ReservationAdminController.class)
@Import({JwtConfig.class, JwtProperties.class})
class ReservationAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private JwtProperties jwtProperties;

    private String cookieKey;

    @BeforeEach
    void init() {
        Mockito.reset(reservationService, authService);
        cookieKey = jwtProperties.getCookieKey();
    }

    @DisplayName("모든 예약 정보를 가져온다.")
    @Test
    void test1() throws Exception {
        // given
        final List<String> names = List.of("꾹", "드라고", "히로");
        final LocalDate now = LocalDate.now();
        final LocalTime time = LocalTime.now();
        final Long timeId = 1L;
        final Long themeId = 1L;
        final AtomicLong id = new AtomicLong(1L);
        final ReservationTimeResponse timeResponse = new ReservationTimeResponse(timeId, time);
        final ThemeResponse themeResponse = new ThemeResponse(themeId, "공포", "묘사", "url");

        final User admin = User.builder()
                .id(1L)
                .password(Password.of("1234"))
                .email("admin@naver.com")
                .username(new Name("꾹"))
                .role(Roles.ADMIN)
                .build();

        final String token = jwtManager.createToken(admin);
        final Cookie cookie = new Cookie(cookieKey, token);

        final List<ReservationResponse> responses = names.stream()
                .map(name -> new ReservationResponse(id.getAndIncrement(), name, now, timeResponse, themeResponse))
                .toList();

        // when
        when(reservationService.getAll(any(), any(), any(), any())).thenReturn(responses);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/reservations")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(names.size()));
    }
}
