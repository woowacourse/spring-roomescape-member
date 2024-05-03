package roomescape.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.TestFixture.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest extends ControllerTest {
    @MockBean
    private ReservationService reservationService;

    @MockBean
    protected ReservationTimeService reservationTimeService;

    @MockBean
    protected ThemeService themeService;

    @Test
    @DisplayName("예약 목록 GET 요청 시 상태코드 200을 반환한다.")
    void findReservations() throws Exception {
        // given
        ReservationTime expectedTime = new ReservationTime(1L, MIA_RESERVATION_TIME);
        Reservation expectedReservation = MIA_RESERVATION(expectedTime, WOOTECO_THEME());

        BDDMockito.given(reservationService.findAll())
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
        ReservationSaveRequest request = new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, 1L, 1L);
        ReservationTime expectedTime = new ReservationTime(1L, MIA_RESERVATION_TIME);
        Theme expectedTheme = WOOTECO_THEME(1L);
        ReservationResponse expectedResponse = ReservationResponse.from(MIA_RESERVATION(expectedTime, expectedTheme));

        BDDMockito.given(reservationService.create(any()))
                .willReturn(expectedResponse);
        BDDMockito.given(reservationTimeService.findById(anyLong()))
                .willReturn(ReservationTimeResponse.from(expectedTime));
        BDDMockito.given(themeService.findById(anyLong()))
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
        ReservationTimeResponse timeResponse = ReservationTimeResponse.from(new ReservationTime(1L, MIA_RESERVATION_TIME));
        ThemeResponse themeResponse = ThemeResponse.from(WOOTECO_THEME(1L));

        BDDMockito.given(reservationService.create(any()))
                .willThrow(BadRequestException.class);
        BDDMockito.given(reservationTimeService.findById(1L))
                .willReturn(timeResponse);
        BDDMockito.given(themeService.findById(1L))
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
        Long notExistingTimeId = 1L;
        Long themeId = 1L;
        ThemeResponse themeResponse = ThemeResponse.from(WOOTECO_THEME(themeId));
        ReservationSaveRequest request = new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, notExistingTimeId, themeId);

        BDDMockito.given(themeService.findById(themeId))
                .willReturn(themeResponse);

        BDDMockito.willThrow(NotFoundException.class)
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
        BDDMockito.willDoNothing()
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
        BDDMockito.willThrow(NotFoundException.class)
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
