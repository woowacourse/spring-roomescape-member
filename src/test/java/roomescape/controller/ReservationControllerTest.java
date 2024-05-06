package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.TestFixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.exception.NotFoundException;
import java.util.List;
import java.util.stream.Stream;


class ReservationControllerTest extends ControllerTest {

    @Test
    @DisplayName("예약 목록 GET 요청 시 상태코드 200을 반환한다.")
    void findReservations() throws Exception {
        // given
        final ReservationTime expectedTime = new ReservationTime(1L, MIA_RESERVATION_TIME);
        final Reservation expectedReservation = MIA_RESERVATION(expectedTime, WOOTECO_THEME());

        given(reservationService.findAll())
                .willReturn(List.of(ReservationResponse.from(expectedReservation)));

        // when & then
        mockMvc.perform(get("/reservations").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(USER_MIA))
                .andExpect(jsonPath("$[0].time.id").value(1L))
                .andExpect(jsonPath("$[0].time.startAt").value(MIA_RESERVATION_TIME))
                .andExpect(jsonPath("$[0].theme.name").value(WOOTECO_THEME_NAME))
                .andExpect(jsonPath("$[0].date").value(MIA_RESERVATION_DATE));
    }

    @Test
    @DisplayName("예약 POST 요청 시 상태코드 201을 반환한다.")
    void createReservation() throws Exception {
        // given
        final ReservationSaveRequest request = new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, 1L, 1L);
        final ReservationTime expectedTime = new ReservationTime(1L, MIA_RESERVATION_TIME);
        final Theme expectedTheme = WOOTECO_THEME(1L);
        final ReservationResponse expectedResponse = ReservationResponse.from(MIA_RESERVATION(expectedTime, expectedTheme));

        given(reservationService.create(any()))
                .willReturn(expectedResponse);
        given(reservationTimeService.findById(anyLong()))
                .willReturn(ReservationTimeResponse.from(expectedTime));
        given(themeService.findById(anyLong()))
                .willReturn(ThemeResponse.from(expectedTheme));

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(USER_MIA))
                .andExpect(jsonPath("$.time.id").value(1L))
                .andExpect(jsonPath("$.time.startAt").value(MIA_RESERVATION_TIME))
                .andExpect(jsonPath("$.date").value(MIA_RESERVATION_DATE));
    }

    @ParameterizedTest
    @MethodSource(value = "invalidPostRequests")
    @DisplayName("잘못된 형식의 예약 POST 요청 시 상태코드 400을 반환한다.")
    void createReservationWithInvalidRequest(ReservationSaveRequest request) throws Exception {
        // given
        final ReservationTimeResponse timeResponse = ReservationTimeResponse.from(new ReservationTime(1L, MIA_RESERVATION_TIME));
        final ThemeResponse themeResponse = ThemeResponse.from(WOOTECO_THEME(1L));

        given(reservationService.create(any()))
                .willThrow(IllegalArgumentException.class);
        given(reservationTimeService.findById(1L))
                .willReturn(timeResponse);
        given(themeService.findById(1L))
                .willReturn(themeResponse);

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    private static Stream<ReservationSaveRequest> invalidPostRequests() {
        return Stream.of(
                new ReservationSaveRequest(null, MIA_RESERVATION_DATE, 1L, 1L),
                new ReservationSaveRequest(USER_MIA, null, 1L, 1L)
        );
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간의 예약 POST 요청 시 상태코드 404를 반환한다.")
    void createReservationWithNotExistTime() throws Exception {
        // given
        final Long notExistingTimeId = 1L;
        final Long themeId = 1L;
        final ThemeResponse themeResponse = ThemeResponse.from(WOOTECO_THEME(themeId));
        final ReservationSaveRequest request = new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, notExistingTimeId, themeId);

        given(themeService.findById(themeId))
                .willReturn(themeResponse);

        willThrow(NotFoundException.class)
                .given(reservationTimeService)
                .findById(anyLong());

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("예약 DELETE 요청 시 상태코드 204를 반환한다.")
    void deleteReservation() throws Exception {
        // given
        willDoNothing()
                .given(reservationService)
                .delete(anyLong());

        // when & then
        mockMvc.perform(delete("/reservations/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 예약 DELETE 요청 시 상태코드 404를 반환한다.")
    void deleteNotExistingReservation() throws Exception {
        // given
        willThrow(NotFoundException.class)
                .given(reservationService)
                .delete(anyLong());

        // when & then
        mockMvc.perform(delete("/reservations/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
