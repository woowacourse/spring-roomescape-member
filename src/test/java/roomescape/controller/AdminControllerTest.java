package roomescape.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.exception.CannotDeleteReservationTimeException;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ReservationService reservationService;
    @MockitoBean
    ReservationTimeService reservationTimeService;
    @MockitoBean
    ThemeService themeService;

    @DisplayName("예약이 존재하는 시간 삭제 요청 시 409 Conflict를 응답한다")
    @Test
    void 삭제하려는_예약_시간에_대한_예약이_존재한다면_409를_응답한다() throws Exception {
        Mockito.doThrow(CannotDeleteReservationTimeException.class)
                .when(reservationTimeService)
                .deleteReservationTime(anyLong());

        mockMvc.perform(delete("/admin/times/1"))
                .andExpect(status().isConflict());
    }
}