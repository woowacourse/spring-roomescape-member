package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.config.CheckAdminInterceptor;
import roomescape.dto.reservation.AdminReservationCreateRequest;
import roomescape.dto.reservation.ReservationFilterRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;
import roomescape.fixture.MemberFixtures;
import roomescape.service.AuthService;
import roomescape.service.ReservationService;

@WebMvcTest(AdminReservationController.class)
class AdminReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReservationService reservationService;
    @MockBean
    private AuthService authService;
    @MockBean
    private CheckAdminInterceptor checkAdminInterceptor;

    @BeforeEach
    void setUp() {
        given(authService.findAuthInfo(anyString())).willReturn(MemberFixtures.createUserMember("daon"));
        given(checkAdminInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any()))
                .willReturn(true);
    }

    @Test
    @DisplayName("예약을 성공적으로 추가한다.")
    void addReservationTest() throws Exception {
        //given
        String expectedName = "daon";
        String expectedDate = "2024-05-05";
        String expectedStartAt = "19:01";
        AdminReservationCreateRequest givenRequest
                = AdminReservationCreateRequest.of(expectedDate, 1L, 1L, 1L);
        ReservationResponse response = ReservationResponse.of(
                1L,
                MemberFixtures.createNameResponse(expectedName),
                expectedDate,
                ReservationTimeResponse.of(1L, expectedStartAt),
                ThemeResponse.of(1L, "방탈출1", "1번 방탈출", "썸네일1")
        );
        given(reservationService.add(any(AdminReservationCreateRequest.class), any(LocalDateTime.class)))
                .willReturn(response);
        String requestBody = objectMapper.writeValueAsString(givenRequest);

        //when //then
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.member.name", is(expectedName)))
                .andExpect(jsonPath("$.date", is(expectedDate)))
                .andExpect(jsonPath("$.time.startAt", is(expectedStartAt)));
    }

    @Test
    @DisplayName("유효하지 않는 값이 입력되면 Bad Request 응답을 반환한다.")
    void createReservationByInvalidRequest() throws Exception {
        //given
        AdminReservationCreateRequest givenRequest
                = AdminReservationCreateRequest.of("InvalidDate", -1L, 1L, 1L);
        given(reservationService.add(any(AdminReservationCreateRequest.class), any(LocalDateTime.class)))
                .willThrow(IllegalArgumentException.class);
        String requestBody = objectMapper.writeValueAsString(givenRequest);

        //when //then
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("필터링한 데이터를 조회하면 200ok를 응답한다.")
    void getFiltered() throws Exception {
        //given
        ReservationFilterRequest filterRequest = new ReservationFilterRequest(1L, 1L, "2024-04-04", "2024-04-05");
        List<ReservationResponse> responses = List.of(
                ReservationResponse.of(
                        1L,
                        MemberFixtures.createNameResponse("daon"),
                        "2024-04-04",
                        ReservationTimeResponse.of(1L, "12:12"),
                        ThemeResponse.of(1L, "방탈출1", "1번 방탈출", "썸네일1")
                ),
                ReservationResponse.of(
                        2L,
                        MemberFixtures.createNameResponse("ikjo"),
                        "2024-04-05",
                        ReservationTimeResponse.of(2L, "12:32"),
                        ThemeResponse.of(1L, "방탈출1", "1번 방탈출", "썸네일1")
                )
        );
        given(reservationService.findFiltered(filterRequest)).willReturn(responses);

        //when //then
        mockMvc.perform(get("/admin/reservations")
                        .param("memberId", String.valueOf(filterRequest.getMemberId()))
                        .param("themeId", String.valueOf(filterRequest.getThemeId()))
                        .param("dateFrom", filterRequest.getDateTo())
                        .param("dateTo", filterRequest.getDateTo()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("필터링한 데이터를 조회시 예외가 발생하면 400 Bad Request를 응답한다.")
    void getFilteredWhenIllegalArgumentException() throws Exception {
        //given
        ReservationFilterRequest filterRequest =
                new ReservationFilterRequest(1L, 1L, null, null);
        given(reservationService.findFiltered(filterRequest)).willThrow(IllegalArgumentException.class);

        //when //then
        mockMvc.perform(get("/admin/reservations")
                        .param("memberId", String.valueOf(filterRequest.getMemberId()))
                        .param("themeId", String.valueOf(filterRequest.getThemeId()))
                        .param("dateFrom", filterRequest.getDateTo())
                        .param("dateTo", filterRequest.getDateTo()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
