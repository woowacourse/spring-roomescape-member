package roomescape.reservation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnauthorizedActionException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationDeleteRequest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationUpdateRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserReservationController.class)
class UserReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void 예약_목록을_조회할_수_있다() throws Exception {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(2L, "Theme A", "desc", "https://example.com/a.png");
        Reservation reservation = new Reservation(3L, "브라운", LocalDate.of(2026, 5, 22), time, theme);

        when(reservationService.findAllByName("브라운")).thenReturn(List.of(reservation));

        // when & then
        mockMvc.perform(get("/reservations")
                        .param("name", "브라운")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("브라운"))
                .andExpect(jsonPath("$[0].date").value("2026-05-22"))
                .andExpect(jsonPath("$[0].time.id").value(1))
                .andExpect(jsonPath("$[0].time.startAt").value("10:00:00"))
                .andExpect(jsonPath("$[0].theme.id").value(2))
                .andExpect(jsonPath("$[0].theme.name").value("Theme A"));
    }

    @Test
    void 예약을_생성할_수_있다() throws Exception {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(2L, "Theme A", "desc", "https://example.com/a.png");
        Reservation reservation = new Reservation(3L, "브라운", LocalDate.of(2026, 5, 1), time, theme);

        ReservationRequest request = new ReservationRequest("브라운", LocalDate.of(2026, 5, 1), 1L, 2L);

        when(reservationService.save(eq("브라운"), eq(LocalDate.of(2026, 5, 1)), eq(1L), eq(2L)))
                .thenReturn(reservation);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("브라운"))
                .andExpect(jsonPath("$.date").value("2026-05-01"))
                .andExpect(jsonPath("$.time.id").value(1))
                .andExpect(jsonPath("$.time.startAt").value("10:00:00"))
                .andExpect(jsonPath("$.theme.id").value(2))
                .andExpect(jsonPath("$.theme.name").value("Theme A"));
    }

    @Test
    void 예약을_삭제할_수_있다() throws Exception {
        ReservationDeleteRequest request = new ReservationDeleteRequest("브라운");

        mockMvc.perform(delete("/reservations/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("성공: 올바른 요청 본문으로 본인의 예약을 수정하면 200 OK와 최신 예약 정보를 반환한다.")
    void 예약_변경_성공() throws Exception {
        // given
        Long reservationId = 1L;
        LocalDate futureDate = LocalDate.now().plusDays(3);
        ReservationUpdateRequest request = new ReservationUpdateRequest("밍구", futureDate, 2L);

        ReservationTime updatedTime = new ReservationTime(2L, LocalTime.of(14, 0));
        Theme theme = new Theme(1L, "우테코 탈출", "설명", "이미지");
        Reservation updatedReservation = new Reservation(reservationId, "밍구", futureDate, updatedTime, theme);

        given(reservationService.updateReservationDateTimeByUser(anyLong(), anyString(), any(LocalDate.class), anyLong()))
                .willReturn(updatedReservation);

        // when & then
        mockMvc.perform(patch("/reservations/{id}", reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationId))
                .andExpect(jsonPath("$.name").value("밍구"))
                .andExpect(jsonPath("$.date").value(futureDate.toString()))
                .andExpect(jsonPath("$.time.id").value(2))
                .andExpect(jsonPath("$.time.startAt").value("14:00:00"));
    }

    @Test
    @DisplayName("예외: 예약자 이름이 일치하지 않는 상태로 수정을 요청하면 401 Unauthorized를 반환한다.")
    void 예약_변경_실패_이름_미일치() throws Exception {
        // given
        Long reservationId = 1L;
        ReservationUpdateRequest request = new ReservationUpdateRequest("틀린이름", LocalDate.now().plusDays(3), 2L);

        given(reservationService.updateReservationDateTimeByUser(anyLong(), anyString(), any(LocalDate.class), anyLong()))
                .willThrow(new UnauthorizedActionException("예약자이름이 일치하지 않아 수정할 수 없습니다."));

        // when & then
        mockMvc.perform(patch("/reservations/{id}", reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("예약자이름이 일치하지 않아 수정할 수 없습니다."));
    }

    @Test
    @DisplayName("예외: 존재하지 않는 예약 ID에 대해 수정을 요청하면 404 Not Found를 반환한다.")
    void 예약_변경_실패_존재하지_않는_예약() throws Exception {
        // given
        Long nonExistentId = 999L;
        ReservationUpdateRequest request = new ReservationUpdateRequest("밍구", LocalDate.now().plusDays(3), 2L);

        given(reservationService.updateReservationDateTimeByUser(anyLong(), anyString(), any(LocalDate.class), anyLong()))
                .willThrow(new NotFoundException("해당 예약을 찾을 수 없습니다. id: " + nonExistentId));

        // when & then
        mockMvc.perform(patch("/reservations/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 예약을 찾을 수 없습니다. id: " + nonExistentId));
    }

    @Test
    @DisplayName("예외: 요청 바디의 필수 필드가 누락되거나 비어있으면 400 Bad Request를 반환한다.")
    void 예약_변경_실패_잘못된_입력값() throws Exception {
        // given
        Long reservationId = 1L;
        ReservationUpdateRequest request = new ReservationUpdateRequest("", LocalDate.now().plusDays(3), 2L);

        // when & then
        mockMvc.perform(patch("/reservations/{id}", reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
