package roomescape.reservation;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

@WebMvcTest(UserReservationController.class)
class UserReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserReservationService userReservationService;

    @Test
    void 예약_목록을_조회할_수_있다() throws Exception {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(2L, "Theme A", "desc", "https://example.com/a.png");
        Reservation reservation = new Reservation(3L, "브라운", LocalDate.of(2026, 5, 1), time, theme);

        when(userReservationService.getReservations()).thenReturn(List.of(reservation));

        mockMvc.perform(get("/reservations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("브라운"))
                .andExpect(jsonPath("$[0].date").value("2026-05-01"))
                .andExpect(jsonPath("$[0].time.id").value(1))
                .andExpect(jsonPath("$[0].time.startAt").value("10:00:00"))
                .andExpect(jsonPath("$[0].theme.id").value(2))
                .andExpect(jsonPath("$[0].theme.name").value("Theme A"));
    }

    @Test
    void 예약을_생성할_수_있다() throws Exception {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(2L, "Theme A", "desc", "https://example.com/a.png");
        Reservation reservation = new Reservation(3L, "브라운", LocalDate.of(2026, 5, 1), time, theme);

        ReservationRequest request = new ReservationRequest(2L, "브라운", LocalDate.of(2026, 5, 1), 1L);

        when(userReservationService.createReservation(eq("브라운"), eq(LocalDate.of(2026, 5, 1)), eq(1L), eq(2L)))
                .thenReturn(reservation);

        mockMvc.perform(post("/reservations")
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
    void 예약을_삭제할_수_있다() throws Exception {
        ReservationDeleteRequest request = new ReservationDeleteRequest("브라운");

        mockMvc.perform(delete("/reservations/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
