package roomescape.reservation.controller;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.common.Role;
import roomescape.config.WebMvcControllerTestConfig;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberName;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationSaveRequest;
import roomescape.reservation.dto.ThemeResponse;
import roomescape.reservation.dto.TimeResponse;
import roomescape.reservation.service.ReservationService;

@WebMvcTest(ReservationApiController.class)
@Import(WebMvcControllerTestConfig.class)
class ReservationApiControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("예약 목록 조회에 성공하면 200 응답을 받는다.")
    void getReservationRequestTest() throws Exception {
        mockMvc.perform(get("/reservations")
                        .cookie(new Cookie("token", "cookieValue"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원이 예약을 성공적으로 추가하면 201 응답과 Location 헤더에 리소스 저장 경로를 받는다.")
    void createMemberReservationRequestTest() throws Exception {
        LoginMember loginMember = new LoginMember(1L, Role.MEMBER, "카키", "kaki@email.com");
        ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest(loginMember.id(), LocalDate.now(), 1L, 1L);

        MemberResponse memberResponse = new MemberResponse(loginMember.id(), loginMember.name());
        ThemeResponse themeResponse = new ThemeResponse(1L, "공포", "무서운 테마", "https://i.pinimg.com/236x.jpg");
        TimeResponse timeResponse = new TimeResponse(1L, LocalTime.now());

        doReturn(1L).when(reservationService)
                .save(reservationSaveRequest, loginMember);

        ReservationResponse reservationResponse = new ReservationResponse(
                1L,
                memberResponse,
                reservationSaveRequest.getDate(),
                themeResponse,
                timeResponse
        );

        doReturn(reservationResponse).when(reservationService)
                .findById(1L);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationSaveRequest))
                        .cookie(new Cookie("token", "cookieValue"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/reservations/1"))
                .andExpect(jsonPath("$.id").value(reservationResponse.id()));
    }

    @Test
    @DisplayName("관리자가 예약을 성공적으로 추가하면 201 응답과 Location 헤더에 리소스 저장 경로를 받는다.")
    void createAdminReservationRequestTest() throws Exception {
        LoginMember loginMember = new LoginMember(1L, Role.ADMIN,"카키", "kaki@email.com");
        ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest(1L, LocalDate.now(), 1L, 1L);

        MemberResponse memberResponse = new MemberResponse(loginMember.id(), loginMember.name());
        ThemeResponse themeResponse = new ThemeResponse(1L, "공포", "무서운 테마", "https://i.pinimg.com/236x.jpg");
        TimeResponse timeResponse = new TimeResponse(1L, LocalTime.now());
        ReservationResponse reservationResponse = new ReservationResponse(
                reservationSaveRequest.getMemberId(),
                memberResponse,
                reservationSaveRequest.getDate(),
                themeResponse, timeResponse
        );

        doReturn(1L).when(reservationService)
                .save(reservationSaveRequest, loginMember);

        doReturn(reservationResponse).when(reservationService)
                .findById(1L);

        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationSaveRequest))
                        .cookie(new Cookie("token", "cookieValue"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/reservations/1"))
                .andExpect(jsonPath("$.id").value(reservationResponse.id()));
    }

    @Test
    @DisplayName("예약을 성공적으로 제거하면 204 응답을 받는다.")
    void deleteReservationRequestTest() throws Exception {
        mockMvc.perform(delete("/reservations/{id}", 1L)
                        .cookie(new Cookie("token", "cookieValue"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
