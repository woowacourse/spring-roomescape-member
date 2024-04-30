package roomescape.reservation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.time.domain.Time;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
    private final Reservation reservation = new Reservation(1L, "polla", LocalDate.now(),
            new Time(1L, LocalTime.now()));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 정보를 잘 저장하는지 확인한다.")
    void saveReservation() throws Exception {
        Mockito.when(reservationService.addReservation(any()))
                .thenReturn(toResponse(reservation));

        String content = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(
                        new ReservationRequest(reservation.getDate(), "polla", 1L));

        mockMvc.perform(post("/reservations")
                        .content(content)
                        .contentType("application/Json")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("예약 정보를 잘 불러오는지 확인한다.")
    void findAllReservations() throws Exception {
        Mockito.when(reservationService.findReservations())
                .thenReturn(List.of(toResponse(reservation)));

        mockMvc.perform(get("/reservations"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("예약 정보를 잘 지우는지 확인한다.")
    void deleteReservation() throws Exception {
        mockMvc.perform(delete("/reservations/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                reservation.getName(), reservation.getDate(), reservation.getReservationTime());
    }
}
