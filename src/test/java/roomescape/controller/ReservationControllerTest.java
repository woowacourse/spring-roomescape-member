package roomescape.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.app.ReservationAppRequest;
import roomescape.dto.web.ReservationTimeWebResponse;
import roomescape.dto.web.ReservationWebRequest;
import roomescape.dto.web.ReservationWebResponse;
import roomescape.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReservationService reservationService;

    @DisplayName("예약을 저장한다. -> 201")
    @Test
    void reserve() throws Exception {
        long time_id = 1L;
        LocalDate date = LocalDate.EPOCH;
        String name = "브리";
        Reservation reservation = new Reservation(1L, name, date, new ReservationTime(LocalTime.MIN));

        when(reservationService.save(new ReservationAppRequest(time_id, date.toString(), name)))
            .thenReturn(reservation);

        String requestBody = objectMapper.writeValueAsString(new ReservationWebRequest(name, date.toString(), time_id));
        String responseBody = objectMapper.writeValueAsString(new ReservationWebResponse(1L, name, date,
            ReservationTimeWebResponse.from(reservation)));

        mvc.perform(post("/reservations")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().json(responseBody));
    }

    @DisplayName("예약을 삭제한다. -> 204")
    @Test
    void deleteBy() throws Exception {
        long time_id = 1L;
        LocalDate date = LocalDate.EPOCH;
        String name = "브리";
        Reservation reservation = new Reservation(1L, name, date, new ReservationTime(LocalTime.MIN));

        when(reservationService.save(new ReservationAppRequest(time_id, date.toString(), name)))
            .thenReturn(reservation);

        mvc.perform(delete("/reservations/" + reservation.getId()))
            .andExpect(status().isNoContent());
    }

    @DisplayName("예약을 조회한다. -> 200")
    @Test
    void getReservations() throws Exception {
        mvc.perform(get("/reservations"))
            .andExpect(status().isOk());
    }
}
