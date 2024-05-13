package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.application.ReservationService;
import roomescape.controller.api.ReservationController;
import roomescape.controller.api.ReservationTimeController;
import roomescape.controller.api.ThemeController;
import roomescape.controller.api.TokenLoginController;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.time.ReservationTime;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.fixture.ThemeFixture;
import roomescape.support.SimpleMockMvc;

@WebMvcTest(controllers = {
        ReservationController.class,
        ReservationTimeController.class,
        ThemeController.class,
        TokenLoginController.class
})
class ReservationControllerTest /*extends ControllerTest*/ {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    private Reservation makeReservation() {
        return new Reservation(
                1L,
                "레모네",
                LocalDate.now().plusDays(2),
                new ReservationTime(1L, LocalTime.parse("12:00")),
                ThemeFixture.theme()
        );
    }

    @Test
    void 예약을_생성한다() throws Exception {
        Reservation reservation = makeReservation();

        when(reservationService.reserve(any())).thenReturn(reservation);
        ReservationRequest request = new ReservationRequest(reservation.getDate(),
                reservation.getTimeId(), reservation.getTheme().getId());

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
                .mapToObj(i -> makeReservation())
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

    @Test
    void 예약_날짜가_형식과_맞지_않으면_Bad_Request_상태를_반환한다() throws Exception {
        String content = "{\"name\":\"prin\", \"date\":\"2024_04_30\", \"timeId\":1}";

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 데이터 형식입니다"))
                .andDo(print());
    }

    @Test
    void 예약_날짜가_올바르지_않으면_Bad_Request_상태를_반환한다() throws Exception {
        String content = "{\"name\":\"lemone\", \"date\":\"2024-04-70\", \"timeId\":1}";

        ResultActions result = SimpleMockMvc.post(mockMvc, "/reservations", content);

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 데이터 형식입니다"))
                .andDo(print());
    }
}
