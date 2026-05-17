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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationLocalDate;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationUpdateRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    private static final ReservationTime TIME = new ReservationTime(1L, LocalTime.of(12, 0));
    private static final Theme THEME = new Theme(1L, new ThemeName("name"), "description",
        ThemeImageUrl.defaultImageUrl());
    private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void 예약을_추가한다() throws Exception {
        // given
        Reservation reservation = reservation();
        ReservationRequest request = requestDtoFrom(reservation);
        when(reservationService.addReservation(any()))
            .thenReturn(reservation.withId(1L));

        // when
        ResultActions result = mockMvc
            .perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L));

        verify(reservationService, times(1)).addReservation(any());
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 지난_날짜와_시간으로_예약을_추가하는_경우_예외_응답을_반환한다() throws Exception {
        // given
        ReservationRequest request = new ReservationRequest(
            "name", LocalDate.now().minusDays(1), 1L, 3L);

        doThrow(new RoomEscapeException(ErrorCode.PAST_DATE_RESERVATION))
            .when(reservationService).addReservation(any());

        // when
        ResultActions result = mockMvc
            .perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.code").value(ErrorCode.PAST_DATE_RESERVATION.name()));

        verify(reservationService, times(1)).addReservation(any());
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 중복_예약이_존재하는_경우_예외_응답을_반환한다() throws Exception {
        // given
        ReservationRequest request = requestDtoFrom(reservation());
        doThrow(new RoomEscapeException(ErrorCode.DUPLICATED_RESERVATION))
            .when(reservationService).addReservation(any());

        // when
        ResultActions result = mockMvc
            .perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATED_RESERVATION.name()));

        verify(reservationService, times(1)).addReservation(any());
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 모든_예약을_조회한다() throws Exception {
        // given
        Reservation reservation = reservation();
        List<Reservation> reservations = List.of(
            reservation.withId(1L), reservation.withId(2L), reservation.withId(3L));
        when(reservationService.getReservations()).thenReturn(reservations);

        // when
        ResultActions result = mockMvc.perform(get("/reservations"));

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[2].id").value(3L));

        verify(reservationService, times(1)).getReservations();
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 사용자_이름으로_해당_사용자의_모든_예약을_조회한다() throws Exception {
        // given
        MemberName targetName = new MemberName("targetName");
        Reservation targetReservation = new Reservation(
            4L, targetName, new ReservationLocalDate(TOMORROW), TIME, THEME);

        when(reservationService.getReservations(any())).thenReturn(List.of(targetReservation));

        // when
        ResultActions result = mockMvc.perform(get("/reservations").queryParam("name", targetName.value()));

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(4L));

        verify(reservationService, times(1)).getReservations(targetName.value());
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 사용자_이름에_해당되는_예약이_없는_경우_빈_리스트_응답을_반환한다() throws Exception {
        // given
        String name = "name";
        when(reservationService.getReservations(any())).thenReturn(List.of());

        // when
        ResultActions result = mockMvc.perform(get("/reservations").queryParam("name", name));

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(reservationService, times(1)).getReservations(name);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 예약을_삭제한다() throws Exception {
        // given
        long id = 1L;
        String name = "korogoo";

        // when
        ResultActions result = mockMvc.perform(delete("/reservations/{id}", id).queryParam("name", name));

        // then
        result.andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteReservation(id, name);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 존재하지_않는_예약을_삭제하는_경우_예외_응답을_반환한다() throws Exception {
        // given
        doThrow(new RoomEscapeException(ErrorCode.RESERVATION_NOT_FOUND))
            .when(reservationService).deleteReservation(anyLong(), any());

        // when
        ResultActions result = mockMvc.perform(delete("/reservations/{id}", 1L).queryParam("name", "korogoo"));

        // then
        result
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(ErrorCode.RESERVATION_NOT_FOUND.name()));

        verify(reservationService, times(1)).deleteReservation(1L, "korogoo");
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 다른_사람의_예약을_삭제하는_경우_예외_응답을_반환한다() throws Exception {
        // given
        doThrow(new RoomEscapeException(ErrorCode.FORBIDDEN))
            .when(reservationService).deleteReservation(anyLong(), any());

        // when
        ResultActions result = mockMvc.perform(delete("/reservations/{id}", 1L).queryParam("name", "korogoo"));

        // then
        result
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code").value(ErrorCode.FORBIDDEN.name()));

        verify(reservationService, times(1)).deleteReservation(1L, "korogoo");
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 오늘_이전의_예약을_삭제하는_경우_예외_응답을_반환한다() throws Exception {
        // given
        doThrow(new RoomEscapeException(ErrorCode.PAST_RESERVATION_CANCEL))
            .when(reservationService).deleteReservation(anyLong(), any());

        // when
        ResultActions result = mockMvc.perform(delete("/reservations/{id}", 1L).queryParam("name", "korogoo"));

        // then
        result
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.code").value(ErrorCode.PAST_RESERVATION_CANCEL.name()));

        verify(reservationService, times(1)).deleteReservation(1L, "korogoo");
        verifyNoMoreInteractions(reservationService);
    }

    @Nested
    @DisplayName("예약 날짜 및 시간 변경")
    class UpdateDateTime {

        private static final long RESERVATION_ID = 1L;
        private static final String REQUESTER_NAME = "korogoo";

        private ReservationUpdateRequest defaultRequest() {
            return new ReservationUpdateRequest(TOMORROW, TIME.getId());
        }

        @Test
        void 사용자는_본인_예약의_날짜_및_시간을_변경할_수_있다() throws Exception {
            // given
            ReservationUpdateRequest request = defaultRequest();

            // when
            ResultActions result = mockMvc
                .perform(patch("/reservations/{id}", RESERVATION_ID)
                    .queryParam("name", REQUESTER_NAME)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            result.andExpect(status().isNoContent());

            verify(reservationService, times(1)).updateDateTime(RESERVATION_ID, REQUESTER_NAME, request);
            verifyNoMoreInteractions(reservationService);
        }

        @Test
        void 예약의_날짜를_과거_날짜로_변경하는_경우_예외_응답을_반환한다() throws Exception {
            // given
            doThrow(new RoomEscapeException(ErrorCode.PAST_RESERVATION_UPDATE))
                .when(reservationService).updateDateTime(anyLong(), any(), any());

            // when
            ResultActions result = mockMvc
                .perform(patch("/reservations/{id}", RESERVATION_ID)
                    .queryParam("name", REQUESTER_NAME)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(defaultRequest())));

            // then
            result.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value(ErrorCode.PAST_RESERVATION_UPDATE.name()));

            verify(reservationService, times(1)).updateDateTime(anyLong(), any(), any());
            verifyNoMoreInteractions(reservationService);
        }

        @Test
        void 존재하지_않는_예약을_변경하는_경우_예외_응답을_반환한다() throws Exception {
            // given
            doThrow(new RoomEscapeException(ErrorCode.RESERVATION_NOT_FOUND))
                .when(reservationService).updateDateTime(anyLong(), any(), any());

            // when
            ResultActions result = mockMvc
                .perform(patch("/reservations/{id}", RESERVATION_ID)
                    .queryParam("name", REQUESTER_NAME)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(defaultRequest())));

            // then
            result
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.RESERVATION_NOT_FOUND.name()));

            verify(reservationService, times(1)).updateDateTime(anyLong(), any(), any());
            verifyNoMoreInteractions(reservationService);
        }

        @Test
        void 예약을_존재하지_않는_시간으로_변경하는_경우_예외_응답을_반환한다() throws Exception {
            // given
            doThrow(new RoomEscapeException(ErrorCode.TIME_NOT_FOUND))
                .when(reservationService).updateDateTime(anyLong(), any(), any());

            // when
            ResultActions result = mockMvc
                .perform(patch("/reservations/{id}", RESERVATION_ID)
                    .queryParam("name", REQUESTER_NAME)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(defaultRequest())));

            // then
            result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.TIME_NOT_FOUND.name()));

            verify(reservationService, times(1)).updateDateTime(anyLong(), any(), any());
            verifyNoMoreInteractions(reservationService);
        }

        @Test
        void 중복되는_예약으로_변경하는_경우_예외_응답을_반환한다() throws Exception {
            // given
            doThrow(new RoomEscapeException(ErrorCode.DUPLICATED_RESERVATION))
                .when(reservationService).updateDateTime(anyLong(), any(), any());

            // when
            ResultActions result = mockMvc
                .perform(patch("/reservations/{id}", RESERVATION_ID)
                    .queryParam("name", REQUESTER_NAME)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(defaultRequest())));

            // then
            result
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATED_RESERVATION.name()));

            verify(reservationService, times(1)).updateDateTime(anyLong(), any(), any());
            verifyNoMoreInteractions(reservationService);
        }

        @Test
        void 다른_사용자의_예약을_변경하는_경우_예외_응답을_반환한다() throws Exception {
            // given
            doThrow(new RoomEscapeException(ErrorCode.FORBIDDEN))
                .when(reservationService).updateDateTime(anyLong(), any(), any());

            // when
            ResultActions result = mockMvc
                .perform(patch("/reservations/{id}", RESERVATION_ID)
                    .queryParam("name", REQUESTER_NAME)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(defaultRequest())));

            // then
            result
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ErrorCode.FORBIDDEN.name()));

            verify(reservationService, times(1)).updateDateTime(anyLong(), any(), any());
            verifyNoMoreInteractions(reservationService);
        }

        @Test
        void 지난_날짜로_예약을_변경하는_경우_예외_응답을_반환한다() throws Exception {
            // given
            doThrow(new RoomEscapeException(ErrorCode.PAST_DATE_RESERVATION))
                .when(reservationService).updateDateTime(anyLong(), any(), any());

            // when
            ResultActions result = mockMvc
                .perform(patch("/reservations/{id}", RESERVATION_ID)
                    .queryParam("name", REQUESTER_NAME)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(defaultRequest())));

            // then
            result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value(ErrorCode.PAST_DATE_RESERVATION.name()));

            verify(reservationService, times(1)).updateDateTime(anyLong(), any(), any());
            verifyNoMoreInteractions(reservationService);
        }
    }

    private Reservation reservation() {
        return new Reservation("이름", LocalDate.now().plusDays(1L), TIME, THEME);
    }

    private ReservationRequest requestDtoFrom(Reservation reservation) {
        return new ReservationRequest(
            reservation.getName().value(),
            reservation.getDateValue(),
            reservation.getTime().getId(),
            reservation.getTheme().getId()
        );
    }
}
