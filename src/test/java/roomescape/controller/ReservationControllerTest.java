package roomescape.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.application.ReservationService;
import roomescape.controller.api.ReservationController;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.time.ReservationTime;
import roomescape.fixture.ThemeFixture;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.support.SimpleMockMvc;
import roomescape.ui.CheckAdminInterceptor;
import roomescape.ui.LoginMemberArgumentResolver;
import roomescape.ui.WebMvcConfiguration;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private WebMvcConfiguration webMvcConfiguration;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private CheckAdminInterceptor checkAdminInterceptor;

    @MockBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;


    private final Member ADMIN = new Member(
            1L,
            new MemberName("어드민"),
            "admin@woo.com",
            "admin",
            MemberRole.ADMIN);

    private Reservation makeReservation() {
        return new Reservation(
                1L,
                ADMIN,
                LocalDate.now().plusDays(2),
                new ReservationTime(1L, LocalTime.parse("12:00")),
                ThemeFixture.theme()
        );
    }

    @Test
    void 전체_예약을_조회한다() throws Exception {
        Reservation reservation = makeReservation();
        when(reservationService.getReservations())
                .thenReturn(List.of(reservation));

        mockMvc.perform(get("/reservations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation.getId()))
                .andExpect(jsonPath("$[0].member.name").value(reservation.getMember().getName().getValue()))
                .andExpect(jsonPath("$[0].time.startAt").value(reservation.getTime().getStartAt().toString()))
                .andExpect(jsonPath("$[0].date").value(reservation.getDate().toString()))
                .andExpect(jsonPath("$[0].theme.name").value(reservation.getTheme().getName()));
    }

    @Test
    void 예약을_취소한다() throws Exception {
        long id = 1L;
        doNothing().when(reservationService).cancel(id);

        ResultActions result = SimpleMockMvc.delete(mockMvc, "/reservations/{id}", id);

        result.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void 예약_날짜가_형식과_맞지_않으면_Bad_Request_상태를_반환한다() throws Exception {
        String content = "{\"date\":\"2024_04_30\", \"timeId\":1}";

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 데이터 형식입니다"))
                .andDo(print());
    }

    @Test
    void 예약_날짜가_올바르지_않으면_Bad_Request_상태를_반환한다() throws Exception {
        String content = "{\"date\":\"2024-04-70\", \"timeId\":1}";

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 데이터 형식입니다"))
                .andDo(print());
    }
}
