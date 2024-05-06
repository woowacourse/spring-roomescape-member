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
                .andExpect(jsonPath("$[0].time.startAt").value(MIA_RESERVATION_TIME.toString()))
                .andExpect(jsonPath("$[0].theme.name").value(WOOTECO_THEME_NAME))
                .andExpect(jsonPath("$[0].date").value(MIA_RESERVATION_DATE.toString()));
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
                .andExpect(jsonPath("$.time.startAt").value(MIA_RESERVATION_TIME.toString()))
                .andExpect(jsonPath("$.date").value(MIA_RESERVATION_DATE.toString()));
    }

    @ParameterizedTest
    @MethodSource(value = "invalidPostRequests")
    @DisplayName("예약 POST 요청 시 하나의 필드라도 없다면 상태코드 400을 반환한다.")
    void createReservationWithNullFieldRequest(ReservationSaveRequest request) throws Exception {
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
                new ReservationSaveRequest(USER_MIA, null, 1L, 1L),
                new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, null, 1L),
                new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, 1L, null)
        );
    }

    @Test
    @DisplayName("올바르지 않은 예약 날짜 형식으로 예약 POST 요청 시 상태코드 400을 반환한다.")
    void createReservationWithInvalidDateFormat() throws Exception {
        // given
        String invalidDateFormatRequest = "{\"date\": \"dfdf\"}";

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidDateFormatRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간의 예약 POST 요청 시 상태코드 404를 반환한다.")
    void createReservationWithNotExistingTime() throws Exception {
        // given
        Long notExistingTimeId = 1L;
        Long themeId = 1L;
        ThemeResponse themeResponse = ThemeResponse.from(WOOTECO_THEME(themeId));
        ReservationSaveRequest request = new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, notExistingTimeId, themeId);

        BDDMockito.given(themeService.findById(themeId))
                .willReturn(themeResponse);

        BDDMockito.willThrow(new NotFoundException(TEST_ERROR_MESSAGE))
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
    @DisplayName("존재하지 않는 테마의 예약 POST 요청 시 상태코드 404를 반환한다.")
    void createReservationWithNotExistingTheme() throws Exception {
        // given
        Long timeId = 1L;
        Long notExistingThemeId = 1L;
        ReservationTimeResponse timeResponse = ReservationTimeResponse.from(new ReservationTime(1L, MIA_RESERVATION_TIME));
        ReservationSaveRequest request = new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, timeId, notExistingThemeId);

        BDDMockito.given(reservationTimeService.findById(timeId))
                .willReturn(timeResponse);

        BDDMockito.willThrow(new NotFoundException(TEST_ERROR_MESSAGE))
                .given(themeService)
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
}
