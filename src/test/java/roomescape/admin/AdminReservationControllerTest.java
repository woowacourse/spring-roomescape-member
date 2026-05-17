package roomescape.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.exception.AlreadyInUseException;
import roomescape.exception.InvalidStateException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.ReservationService;

@WebMvcTest(AdminReservationController.class)
class AdminReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void 예약_삭제() throws Exception {
        mockMvc.perform(delete("/api/admin/reservations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void 지난_날짜_예약_삭제시_400() throws Exception {
        willThrow(new InvalidStateException("이미 지난 날짜와 시간입니다."))
                .given(reservationService).deleteByAdmin(1L);

        mockMvc.perform(delete("/api/admin/reservations/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 존재하지_않는_예약_삭제시_404() throws Exception {
        willThrow(new NotFoundException("예약을 찾을 수 없습니다."))
                .given(reservationService).deleteByAdmin(1L);

        mockMvc.perform(delete("/api/admin/reservations/1"))
                .andExpect(status().isNotFound());
    }
}
