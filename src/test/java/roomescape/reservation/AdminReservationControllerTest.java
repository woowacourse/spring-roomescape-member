package roomescape.reservation;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

@WebMvcTest(AdminReservationController.class)
class AdminReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminReservationService adminReservationService;

    @Test
    void 관리자가_예약을_생성할_수_있다() throws Exception {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(2L, "Theme A", "desc", "thumb");
        Reservation reservation = new Reservation(3L, "브라운", LocalDate.of(2026, 5, 1), time, theme);

        ReservationRequest request = new ReservationRequest(2L, "브라운", LocalDate.of(2026, 5, 1), 1L);

        when(adminReservationService.createForceReservation(eq(2L), eq("브라운"), eq(LocalDate.of(2026, 5, 1)), eq(1L)))
                .thenReturn(reservation);

        mockMvc.perform(post("/admin/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("브라운"))
                .andExpect(jsonPath("$.date").value("2026-05-01"))
                .andExpect(jsonPath("$.time.id").value(1))
                .andExpect(jsonPath("$.time.startAt").value("10:00:00"))
                .andExpect(jsonPath("$.theme.id").value(2))
                .andExpect(jsonPath("$.theme.name").value("Theme A"));
    }

    @Test
    void 관리자가_예약을_삭제할_수_있다() throws Exception {
        mockMvc.perform(delete("/admin/reservations/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
