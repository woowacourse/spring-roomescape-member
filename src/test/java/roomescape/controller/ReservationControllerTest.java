package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.TestFixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.MemberResponse;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.exception.NotFoundException;
import java.util.List;

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
                .andExpect(jsonPath("$[0].name").value(MEMBER_MIA_NAME))
                .andExpect(jsonPath("$[0].time.id").value(1L))
                .andExpect(jsonPath("$[0].time.startAt").value(MIA_RESERVATION_TIME))
                .andExpect(jsonPath("$[0].theme.name").value(WOOTECO_THEME_NAME))
                .andExpect(jsonPath("$[0].date").value(MIA_RESERVATION_DATE));
    }

    @Test
    @DisplayName("예약 POST 요청 시 상태코드 201을 반환한다.")
    void createReservation() throws Exception {
        // given
        final ReservationSaveRequest request = new ReservationSaveRequest(1L, "2034-05-08", 1L, 1L);
        final Member member = new Member(1L, new Name("냥인"), "nyangin@email.com", "1234");
        final ReservationTime expectedTime = new ReservationTime(1L, "19:00");
        final Theme expectedTheme = new Theme(1L, "호러", "매우 무섭습니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Reservation reservation = new Reservation(1L, member, "2034-05-08", expectedTime, expectedTheme);
        final ReservationResponse expectedResponse = ReservationResponse.from(reservation);

        given(reservationService.create(any()))
                .willReturn(expectedResponse);
        given(reservationTimeService.findById(anyLong()))
                .willReturn(ReservationTimeResponse.from(expectedTime));
        given(themeService.findById(anyLong()))
                .willReturn(ThemeResponse.from(expectedTheme));
        given(memberService.findById(anyLong()))
                .willReturn(MemberResponse.from(member));

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("냥인"))
                .andExpect(jsonPath("$.time.id").value(1L))
                .andExpect(jsonPath("$.time.startAt").value("19:00"))
                .andExpect(jsonPath("$.date").value("2034-05-08"))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간의 예약 POST 요청 시 상태코드 404를 반환한다.")
    void createReservationWithNotExistTime() throws Exception {
        // given
        final Long memberId = 1L;
        final Long notExistingTimeId = 1L;
        final Long themeId = 1L;
        final ThemeResponse themeResponse = ThemeResponse.from(WOOTECO_THEME(themeId));
        final ReservationSaveRequest request = new ReservationSaveRequest(memberId, MIA_RESERVATION_DATE, notExistingTimeId, themeId);

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
