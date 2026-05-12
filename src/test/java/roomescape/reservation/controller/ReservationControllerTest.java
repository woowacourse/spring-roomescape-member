package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.service.ReservationService;
import roomescape.theme.domain.Theme;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void getAll() throws Exception {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        Theme theme = new Theme("테마", "설명", "https://img.test/a.png").withId(1L);
        Reservation reservation = new Reservation("라이", LocalDate.of(2026, 5, 20), time, 1L)
                .withId(1L)
                .withTheme(theme);
        Mockito.when(reservationService.getAll()).thenReturn(List.of(reservation));

        // when & then
        mockMvc.perform(get("/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("라이"));
    }

    @Test
    void create() throws Exception {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        Theme theme = new Theme("테마", "설명", "https://img.test/a.png").withId(1L);
        Reservation saved = new Reservation("라이", LocalDate.of(2026, 5, 20), time, 1L)
                .withId(1L)
                .withTheme(theme);
        Mockito.when(reservationService.create(Mockito.any())).thenReturn(saved);

        String requestBody = """
                {
                    "name": "라이",
                    "date": "2026-05-20",
                    "themeId": 1,
                    "timeId": 1
                }
                """;

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("라이"));
    }

    @Test
    void cancel() throws Exception {
        // when & then
        mockMvc.perform(delete("/reservations/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(reservationService).cancel(1L);
    }
}
