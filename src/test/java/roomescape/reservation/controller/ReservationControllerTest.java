package roomescape.reservation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationTimeAvailabilityResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    private static final LocalDate TODAY = LocalDate.now();

    private final Reservation reservation = Reservation.reservationOf(1L, "polla", TODAY,
            new Time(1L, LocalTime.of(10, 0)), new Theme(1L, "polla", "폴라 방탈출", "이미지~"));
    private final String expectedStartAt = "10:00:00";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 정보를 잘 저장하는지 확인한다.")
    void saveReservation() throws Exception {
        when(reservationService.addReservation(any()))
                .thenReturn(ReservationResponse.fromReservation(reservation));

        String content = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(new ReservationRequest(reservation.getDate(), "polla", 1L, 1L));

        mockMvc.perform(post("/reservations")
                        .content(content)
                        .contentType("application/Json")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.reservationName").value(reservation.getName()))
                .andExpect(jsonPath("$.startAt").value(expectedStartAt))
                .andExpect(jsonPath("$.themeName").value(reservation.getTheme().getName()));
    }

    @Test
    @DisplayName("예약 정보를 잘 불러오는지 확인한다.")
    void findAllReservations() throws Exception {
        when(reservationService.findReservations())
                .thenReturn(List.of(ReservationResponse.fromReservation(reservation)));

        mockMvc.perform(get("/reservations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation.getId()))
                .andExpect(jsonPath("$[0].reservationName").value(reservation.getName()))
                .andExpect(jsonPath("$[0].startAt").value(expectedStartAt))
                .andExpect(jsonPath("$[0].themeName").value(reservation.getTheme().getName()));
        ;
    }

    @Test
    @DisplayName("예약 가능한 시간을 잘 불러오는지 확인한다.")
    void findAvailableTimeList() throws Exception {
        when(reservationService.findTimeAvailability(1, TODAY))
                .thenReturn(
                        List.of(ReservationTimeAvailabilityResponse.fromTime(reservation.getReservationTime(), true)));

        mockMvc.perform(get("/reservations/1?date=" + TODAY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startAt").value(expectedStartAt))
                .andExpect(jsonPath("$[0].timeId").value(reservation.getReservationTime().getId()))
                .andExpect(jsonPath("$[0].alreadyBooked").value(true));
    }

    @Test
    @DisplayName("예약 정보를 잘 지우는지 확인한다.")
    void deleteReservation() throws Exception {
        mockMvc.perform(delete("/reservations/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
