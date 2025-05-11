package roomescape.domain.reservation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.auth.config.JwtConfig;
import roomescape.domain.auth.config.JwtProperties;
import roomescape.domain.auth.dto.LoginUserDto;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.service.AuthService;
import roomescape.domain.auth.service.JwtManager;
import roomescape.domain.reservation.dto.ReservationCreateRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationTimeResponse;
import roomescape.domain.reservation.dto.ThemeResponse;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.utils.PasswordFixture;

@WebMvcTest(ReservationController.class)
@Import({JwtConfig.class, JwtProperties.class})
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationController reservationController;

    @MockitoBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private JwtManager jwtManager;

    private static String formatStartAt(final LocalTime time) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return time.format(formatter);
    }

    @BeforeEach
    void init() {
        Mockito.reset(reservationService, authService);
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

        final List<ReservationResponse> responses = names.stream()
                .map(name -> new ReservationResponse(id.getAndIncrement(), name, now, timeResponse, themeResponse))
                .toList();

        // when
        when(reservationService.getAll(any(), any(), any(), any())).thenReturn(responses);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(names.size()));
    }

    @DisplayName("예약 정보를 추가한다.")
    @Test
    void test2() throws Exception {
        final Long reservationId = 1L;
        final Long timeId = 1L;
        final Long themeId = 1L;

        final LocalDate date = LocalDate.now();
        final LocalTime time = LocalTime.of(8, 0);
        final String name = "꾹이";

        final ReservationCreateRequest request = new ReservationCreateRequest(date, timeId, themeId);
        final ReservationTimeResponse timeResponse = new ReservationTimeResponse(timeId, time);
        final ThemeResponse themeResponse = new ThemeResponse(themeId, "공포", "묘사", "url");
        final ReservationResponse response = new ReservationResponse(reservationId, name, date, timeResponse,
                themeResponse);

        final String requestContent = objectMapper.writeValueAsString(request);

        final User user = new User(1L, new Name(name), "2321@naver.com", PasswordFixture.generate(), Roles.USER);
        final String token = jwtManager.createToken(user);
        final Cookie cookie = new Cookie("token", token);
        final LoginUserDto loginUserDto = mock(LoginUserDto.class);

        when(authService.getLoginUser((Cookie[]) any())).thenReturn(loginUserDto);
        when(reservationService.create(eq(request), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reservationId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.date").value(date.toString()))
                .andExpect(jsonPath("$.time.id").value(timeId))
                .andExpect(jsonPath("$.time.startAt").value(formatStartAt(time)));
    }

    @DisplayName("존재하지 않는 TimeId면 404 상태 코드를 반환한다.")
    @Test
    void test4() throws Exception {
        // given
        final Long timeId = 1L;
        final Long themeId = 1L;
        final LocalDate date = LocalDate.now();

        final ReservationCreateRequest request = new ReservationCreateRequest(date, timeId, themeId);

        final String requestContent = objectMapper.writeValueAsString(request);
        final String name = "꾹이";
        final User user = new User(1L, new Name(name), "2321@naver.com", PasswordFixture.generate(), Roles.USER);
        final String token = jwtManager.createToken(user);
        final Cookie cookie = new Cookie("token", token);
        final LoginUserDto loginUserDto = mock(LoginUserDto.class);

        // when
        when(authService.getLoginUser((Cookie[]) any())).thenReturn(loginUserDto);
        when(reservationService.create(any(), any())).thenThrow(EntityNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isNotFound());
    }

    @DisplayName("인증 쿠키가 존재하지 않을 경우 401 상태 코드를 반환한다.")
    @Test
    void test3() throws Exception {
        // given
        final Long timeId = 1L;
        final Long themeId = 1L;
        final LocalDate date = LocalDate.now();

        final ReservationCreateRequest request = new ReservationCreateRequest(date, timeId, themeId);

        final String requestContent = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("예약 정보를 삭제한다.")
    @Test
    void test5() throws Exception {
        // given
        final long reservationId = 1;

        // when
        doNothing().when(reservationService)
                .delete(any());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/" + reservationId))
                .andExpect(status().is(204));
    }

    @DisplayName("삭제할 예약 정보가 존재하지 않는다면 404 상태 코드를 반환한다.")
    @Test
    void test6() throws Exception {
        // given
        final long reservationId = 1;

        // when
        doThrow(EntityNotFoundException.class).when(reservationService)
                .delete(any());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/" + reservationId))
                .andExpect(status().isNotFound());
    }

    @DisplayName("컨트롤러 jdbc 분리 테스트")
    @Test
    void jdbcBeanTest() {
        boolean isJdbcTemplateInjected = false;

        for (final Field field : reservationController.getClass()
                .getDeclaredFields()) {
            if (field.getType()
                    .equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }
}
