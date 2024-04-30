package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.application.ReservationService;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.fixture.ReservationFixture;
import roomescape.support.ControllerTest;
import roomescape.support.SimpleMockMvc;

class ReservationControllerTest extends ControllerTest {
    @Autowired
    private ReservationService reservationService;

    @Test
    void 예약을_생성한다() throws Exception {
        Reservation reservation = ReservationFixture.reservation();
        when(reservationService.reserve(any())).thenReturn(reservation);
        ReservationRequest request = new ReservationRequest(reservation.getName(), reservation.getDate(),
                reservation.getTimeId());
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(reservation.getId()),
                        jsonPath("$.name").value(reservation.getName()),
                        jsonPath("$.date").value(reservation.getDate().toString()),
                        jsonPath("$.time.id").value(reservation.getTimeId()),
                        jsonPath("$.time.startAt").value(reservation.getTime().toString())
                )
                .andDo(print());
    }

    @Test
    void 전체_예약을_조회한다() throws Exception {
        List<Reservation> reservations = IntStream.range(0, 3)
                .mapToObj(ReservationFixture::reservation)
                .toList();
        when(reservationService.getReservations()).thenReturn(reservations);

        ResultActions result = SimpleMockMvc.get(mockMvc, "/reservations");

        result.andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(reservations.get(0).getId()),
                        jsonPath("$[1].id").value(reservations.get(1).getId()),
                        jsonPath("$[2].id").value(reservations.get(2).getId())
                )
                .andDo(print());
    }

    @Test
    void 예약을_취소한다() throws Exception {
        long id = 1L;
        doNothing().when(reservationService).cancel(id);

        ResultActions result = SimpleMockMvc.delete(mockMvc, "/reservations/{id}", id);

        result.andExpect(status().isNoContent())
                .andDo(print());
    }
}
