package roomescape.controller.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import roomescape.controller.ControllerTest;
import roomescape.controller.theme.ReservationThemeResponse;
import roomescape.controller.time.TimeResponse;
import roomescape.service.ReservationService;
import roomescape.service.exception.PreviousTimeException;
import roomescape.service.exception.ReservationDuplicatedException;
import roomescape.service.exception.ThemeNotFoundException;
import roomescape.service.exception.TimeNotFoundException;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest extends ControllerTest {

    @Autowired
    ReservationController reservationController;

    @MockBean
    ReservationService reservationService;

    @Test
    @DisplayName("예약 목록을 조회하면 200 과 예약 리스트를 응답한다.")
    void getReservations200AndReservations() throws Exception {
        // given
        final List<ReservationResponse> response = List.of(
                new ReservationResponse(
                        1L,
                        "Name 1",
                        "2024-05-05",
                        new TimeResponse(1L, "08:00", false),
                        new ReservationThemeResponse(1L, "Theme 1")
                )
        );
        final String responseJson = objectMapper.writeValueAsString(response);

        Mockito.when(reservationService.getReservations())
                .thenReturn(response);

        // when & then
        mvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("예약을 추가하면 201 과 예약 정보를 응답한다.")
    void addReservation200AndReservation() throws Exception {
        // given
        final ReservationRequest request = new ReservationRequest("Seyang", "2024-05-05", 1L, 1L);
        final String requestJson = objectMapper.writeValueAsString(request);
        final ReservationResponse response = new ReservationResponse(
                1L, request.name(),
                request.date(),
                new TimeResponse(1L, "08:00", false),
                new ReservationThemeResponse(1L, "Theme 1")
        );
        final String responseJson = objectMapper.writeValueAsString(response);

        Mockito.when(reservationService.addReservation(request))
                .thenReturn(response);

        // when & then
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/reservations/1"))
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약을 추가하면 404 을 응답한다.")
    void addReservation400TimeNotFound() throws Exception {
        // given
        final ReservationRequest request = new ReservationRequest("Seyang", "2024-05-05", 1L, 1L);
        final String requestJson = objectMapper.writeValueAsString(request);

        final String message = "존재하지 않은 시간입니다.";
        Mockito.when(reservationService.addReservation(request))
                .thenThrow(new TimeNotFoundException(message));

        // when & then
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(message)));
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약을 추가하면 404 을 응답한다.")
    void addReservation400ThemeNotFound() throws Exception {
        // given
        final ReservationRequest request = new ReservationRequest("Seyang", "2024-05-05", 1L, 1L);
        final String requestJson = objectMapper.writeValueAsString(request);

        final String message = "존재하지 않는 테마입니다.";
        Mockito.when(reservationService.addReservation(request))
                .thenThrow(new ThemeNotFoundException(message));

        // when & then
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(message)));

    }

    @Test
    @DisplayName("이미 예약이 된 테마와 날짜 및 시간으로 예약을 추가하면 409을 응답한다.")
    void addReservation400Duplicated() throws Exception {
        // given
        final ReservationRequest request = new ReservationRequest("Seyang", "2024-05-05", 1L, 1L);
        final String requestJson = objectMapper.writeValueAsString(request);

        final String message = "중복된 시간으로 예약이 불가합니다.";

        Mockito.when(reservationService.addReservation(request))
                .thenThrow(new ReservationDuplicatedException(message));

        // when & then
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString(message)));
    }

    @Test
    @DisplayName("지난 시간으로 예약을 추가하면 400 을 응답한다.")
    void aadReservation400PreviousTime() throws Exception {
        // given
        final ReservationRequest request = new ReservationRequest("Seyang", "2024-05-05", 1L, 1L);
        final String requestJson = objectMapper.writeValueAsString(request);

        final String message = "지난 시간으로 예약할 수 없습니다.";

        Mockito.when(reservationService.addReservation(request))
                .thenThrow(new PreviousTimeException(message));

        // when & then
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(message)));
    }

    @Test
    @DisplayName("존재하는 예약을 삭제하면 204 를 응답한다.")
    void deleteReservations204() throws Exception {
        // given
        Mockito.when(reservationService.deleteReservation(Mockito.anyLong()))
                .thenReturn(1);

        // then & when
        mvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하면 404 를 응답한다.")
    void deleteReservations404() throws Exception {
        // given
        Mockito.when(reservationService.deleteReservation(Mockito.anyLong()))
                .thenReturn(0);

        // when & then
        mvc.perform(delete("/reservations/1"))
                .andExpect(status().isNotFound());
    }
}
