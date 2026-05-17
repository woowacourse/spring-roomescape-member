package roomescape.reservation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.AdminReservationService;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminReservationController.class)
class AdminReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminReservationService adminReservationService;

    @Test
    void 예약을_강제_생성한다() throws Exception {
        Reservation reservation = new Reservation(1L, "테스트", LocalDate.of(2099, 12, 31),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "테마", "설명", "썸네일"));
        given(adminReservationService.forceCreateReservation(anyLong(), anyString(), any(), anyLong()))
                .willReturn(reservation);

        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"themeId":1,"name":"테스트","date":"2099-12-31","timeId":1}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void 예약_강제생성시_themeId가_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"테스트","date":"2099-12-31","timeId":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_강제생성시_name이_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"themeId":1,"date":"2099-12-31","timeId":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_강제생성시_name이_공백이면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"themeId":1,"name":"   ","date":"2099-12-31","timeId":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_강제생성시_date가_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"themeId":1,"name":"테스트","timeId":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_강제생성시_timeId가_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"themeId":1,"name":"테스트","date":"2099-12-31"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
