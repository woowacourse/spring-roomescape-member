package roomescape.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationTime.ReservationTimeRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.service.ReservationService;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerTest {

    private static final long TIME_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void 관리자는_예약_시간을_추가할_수_있다() throws Exception {
        // given
        ReservationTime newTime = reservationTime();
        ReservationTimeRequest request = requestDtoFrom(newTime);
        when(reservationService.addReservationTime(any())).thenReturn(newTime);

        // when
        ResultActions result = mockMvc
            .perform(post("/times")
                .queryParam("role", "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(TIME_ID));

        verify(reservationService, times(1)).addReservationTime(any());
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 전체_예약_시간을_조회한다() throws Exception {
        // given
        List<ReservationTime> times = List.of(
            new ReservationTime(1L, LocalTime.of(12, 30)),
            new ReservationTime(2L, LocalTime.of(14, 30)));
        when(reservationService.getReservationTimes()).thenReturn(times);

        // when
        ResultActions result = mockMvc.perform(get("/times"));

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[1].id").value(2L));

        verify(reservationService, times(1)).getReservationTimes();
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 관리자는_예약_시간을_삭제할_수_있다() throws Exception {
        // given & when
        ResultActions result = mockMvc
            .perform(delete("/times/{id}", TIME_ID).queryParam("role", "admin"));

        // then
        result.andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteReservationTime(TIME_ID);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 오늘_이후의_예약이_사용하는_시간을_삭제하면_예외가_발생한다() throws Exception {
        // given
        doThrow(new RoomEscapeException(ErrorCode.TIME_HAS_RESERVATIONS))
            .when(reservationService).deleteReservationTime(anyLong());

        // when
        ResultActions result = mockMvc
            .perform(delete("/times/{id}", TIME_ID).queryParam("role", "admin"));

        // then
        result
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(ErrorCode.TIME_HAS_RESERVATIONS.name()));

        verify(reservationService, times(1)).deleteReservationTime(TIME_ID);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 날짜와_테마아이디로_예약가능한_시간을_조회한다() throws Exception {
        // given
        LocalDate date = LocalDate.parse("2026-05-05");
        ReservationTime availableTime = reservationTime();
        ReservationTime impossibleTime = new ReservationTime(2L, LocalTime.of(14, 30));

        when(reservationService.getAvailableTimes(any(), anyLong())).thenReturn(List.of(availableTime));
        when(reservationService.getReservationTimes()).thenReturn(List.of(availableTime, impossibleTime));

        // when
        ResultActions result = mockMvc
            .perform(get("/times/available")
                .queryParam("date", date.toString())
                .queryParam("themeId", "1"));

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.times", hasSize(2)))
            .andExpect(jsonPath("$.times[0].id").value(1L))
            .andExpect(jsonPath("$.times[0].available").value(true))
            .andExpect(jsonPath("$.times[1].id").value(2L))
            .andExpect(jsonPath("$.times[1].available").value(false));

        verify(reservationService, times(1)).getAvailableTimes(date, 1L);
        verify(reservationService, times(1)).getReservationTimes();
        verifyNoMoreInteractions(reservationService);
    }

    @Nested
    @DisplayName("인가 권한이 없는 경우 예외가 발생한다")
    class RoleForbidden {

        @Test
        void 관리자가_아닌_사용자가_시간을_추가하는_경우_예외가_발생한다() throws Exception {
            // given
            ReservationTimeRequest request = requestDtoFrom(reservationTime());

            // when
            ResultActions result = mockMvc
                .perform(post("/times")
                    .queryParam("role", "user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            result.andExpect(status().isForbidden());

            verifyNoMoreInteractions(reservationService);
        }

        @Test
        void 관리자가_아닌_사용자가_테마를_삭제하는_경우_예외가_발생한다() throws Exception {
            // given & when
            ResultActions result = mockMvc
                .perform(delete("/times/{id}", TIME_ID).queryParam("role", "user"));

            // then
            result.andExpect(status().isForbidden());

            verifyNoMoreInteractions(reservationService);
        }
    }

    private ReservationTime reservationTime() {
        return new ReservationTime(TIME_ID, LocalTime.of(12, 30));
    }

    private ReservationTimeRequest requestDtoFrom(ReservationTime time) {
        return new ReservationTimeRequest(time.getStartAt());
    }
}
