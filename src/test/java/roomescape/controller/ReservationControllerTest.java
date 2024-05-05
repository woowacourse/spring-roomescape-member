package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.controller.request.ReservationWebRequest;
import roomescape.controller.response.ReservationWebResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.PastReservationException;
import roomescape.service.ReservationService;
import roomescape.service.request.ReservationAppRequest;
import roomescape.service.response.ReservationAppResponse;

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
        long timeId = 1L;
        long themeId = 1L;
        ReservationDate date = new ReservationDate("2040-04-04");
        String name = "브리";

        Reservation reservation = new Reservation(1L, name, date, new ReservationTime(LocalTime.MIN.toString()),
            new Theme("방탈출", "방탈출하는 게임",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        ReservationAppResponse appResponse = ReservationAppResponse.from(reservation);

        when(reservationService.save(new ReservationAppRequest(name, date.toString(), timeId, themeId)))
            .thenReturn(appResponse);

        String requestBody = objectMapper.writeValueAsString(
            new ReservationWebRequest(name, date.toString(), timeId, themeId));
        String responseBody = objectMapper.writeValueAsString(ReservationWebResponse.from(appResponse));

        mvc.perform(post("/reservations")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().json(responseBody));
    }

    @DisplayName("예약을 삭제한다. -> 204")
    @Test
    void deleteBy() throws Exception {
        mvc.perform(delete("/reservations/" + 1L))
            .andExpect(status().isNoContent());
    }

    @DisplayName("예약을 조회한다. -> 200")
    @Test
    void getReservations() throws Exception {
        mvc.perform(get("/reservations"))
            .andExpect(status().isOk());
    }

    @DisplayName("실패: 예약 추가에서 IllegalArgumentException 발생 시 -> 400")
    @Test
    void reserve_Illegal() throws Exception {
        long timeId = 1L;
        long themeId = 1L;
        ReservationDate date = new ReservationDate("2040-04-04");
        String name = "브리";

        when(reservationService.save(any(ReservationAppRequest.class)))
            .thenThrow(IllegalArgumentException.class);

        String requestBody = objectMapper.writeValueAsString(
            new ReservationWebRequest(name, date.toString(), timeId, themeId));

        mvc.perform(post("/reservations")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @DisplayName("실패: 예약 추가에서 NoSuchElementException 발생 시 -> 400")
    @Test
    void reserve_NoSuch() throws Exception {
        long timeId = 1L;
        long themeId = 1L;
        ReservationDate date = new ReservationDate("2040-04-04");
        String name = "브리";

        when(reservationService.save(any(ReservationAppRequest.class)))
            .thenThrow(NoSuchElementException.class);

        String requestBody = objectMapper.writeValueAsString(
            new ReservationWebRequest(name, date.toString(), timeId, themeId));

        mvc.perform(post("/reservations")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @DisplayName("과거 시간에 예약을 넣을 경우 -> 400")
    @Test
    void reserve_PastTime() throws Exception {
        long timeId = 1L;
        long themeId = 1L;
        ReservationDate date = new ReservationDate("1040-04-04");
        String name = "브리";

        when(reservationService.save(any(ReservationAppRequest.class)))
            .thenThrow(PastReservationException.class);

        String requestBody = objectMapper.writeValueAsString(
            new ReservationWebRequest(name, date.toString(), timeId, themeId));

        mvc.perform(post("/reservations")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @DisplayName("실패: 예약 추가에서 MethodArgumentNotValidException 발생 시 -> 400")
    @Test
    void reserve_MethodArgNotValidException() throws Exception {
        ReservationWebRequest request = new ReservationWebRequest(null, "2040-04-04", 1L, 1L);
        
        String requestBody = objectMapper.writeValueAsString(
            request);

        mvc.perform(post("/reservations")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
