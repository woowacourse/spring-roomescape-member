package roomescape.reservation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.UserReservationService;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserReservationController.class)
class UserReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserReservationService userReservationService;

    @Test
    void 예약을_정상적으로_생성한다() throws Exception {
        Reservation reservation = new Reservation(1L, "테스트", LocalDate.of(2099, 12, 31),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "테마", "설명", "썸네일"));
        given(userReservationService.createReservation(anyString(), any(), anyLong(), anyLong()))
                .willReturn(reservation);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"themeId":1,"name":"테스트","date":"2099-12-31","timeId":1}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void 예약_생성시_themeId가_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"테스트","date":"2099-12-31","timeId":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_생성시_name이_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"themeId":1,"date":"2099-12-31","timeId":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_생성시_name이_공백이면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"themeId":1,"name":"   ","date":"2099-12-31","timeId":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_생성시_date가_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"themeId":1,"name":"테스트","timeId":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_생성시_timeId가_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"themeId":1,"name":"테스트","date":"2099-12-31"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_수정시_date가_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(patch("/reservations/1")
                        .header("X-User-Name", "테스트")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"timeId":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_수정시_timeId가_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(patch("/reservations/1")
                        .header("X-User-Name", "테스트")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"date":"2099-12-31"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
