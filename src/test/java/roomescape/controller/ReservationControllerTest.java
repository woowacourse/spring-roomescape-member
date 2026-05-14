package roomescape.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.ForbiddenReservationException;
import roomescape.exception.NotFoundException;
import roomescape.exception.PastReservationException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void 사용자_예약을_생성한다() throws Exception {
        // given
        given(reservationService.createUserReservation(
                eq("브라운"),
                eq(LocalDate.of(2099, 1, 1)),
                eq(1L),
                eq(1L)))
                .willReturn(reservation());

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/reservations/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("브라운"))
                .andExpect(jsonPath("$.date").value("2099-01-01"))
                .andExpect(jsonPath("$.time.id").value(1))
                .andExpect(jsonPath("$.time.startAt").value("10:00:00"))
                .andExpect(jsonPath("$.theme.id").value(1))
                .andExpect(jsonPath("$.theme.name").value("테마"));
    }

    @Test
    void 사용자_본인_예약을_조회한다() throws Exception {
        // given
        given(reservationService.findByName(eq("브라운")))
                .willReturn(List.of(reservation()));

        // when & then
        mockMvc.perform(get("/reservations")
                        .param("name", "브라운"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("브라운"))
                .andExpect(jsonPath("$[0].date").value("2099-01-01"))
                .andExpect(jsonPath("$[0].time.id").value(1))
                .andExpect(jsonPath("$[0].time.startAt").value("10:00:00"))
                .andExpect(jsonPath("$[0].theme.id").value(1))
                .andExpect(jsonPath("$[0].theme.name").value("테마"));
    }

    @Test
    void 사용자_본인_예약_조회시_이름이_없으면_에러_응답() throws Exception {
        // when & then
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.detail").value("name는 필수입니다."));
    }

    @Test
    void 사용자_본인_예약_조회시_이름이_비어있으면_에러_응답() throws Exception {
        // when & then
        mockMvc.perform(get("/reservations")
                        .param("name", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.detail").value("name은 비어 있을 수 없습니다."));
    }

    @Test
    void 사용자_본인_예약을_취소한다() throws Exception {
        // given
        Long id = 1L;
        String name = "브라운";

        // when & then
        mockMvc.perform(delete("/reservations/{id}", id)
                        .param("name", name))
                .andExpect(status().isNoContent());

        verify(reservationService).deleteUserReservation(id, name);
    }

    @Test
    void 사용자_본인_예약_취소시_이름이_없으면_에러_응답() throws Exception {
        // when & then
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.detail").value("name는 필수입니다."));
    }

    @Test
    void 사용자_본인_예약_취소시_이름이_비어있으면_에러_응답() throws Exception {
        // when & then
        mockMvc.perform(delete("/reservations/1")
                        .param("name", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.detail").value("name은 비어 있을 수 없습니다."));
    }

    @Test
    void 사용자_본인_예약_취소시_id가_양수가_아니면_에러_응답() throws Exception {
        // when & then
        mockMvc.perform(delete("/reservations/0")
                        .param("name", "브라운"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.detail").value("id는 양수이어야 합니다."));
    }

    @Test
    void 사용자_본인_예약_취소시_id_형식이_올바르지_않으면_에러_응답() throws Exception {
        // when & then
        mockMvc.perform(delete("/reservations/abc")
                        .param("name", "브라운"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.detail").value("id 형식이 올바르지 않습니다."));
    }

    @Test
    void 사용자_본인_예약_취소시_본인의_예약이_아니면_에러_응답() throws Exception {
        // given
        Long id = 1L;
        String name = "브라운";
        willThrow(new ForbiddenReservationException("본인의 예약만 변경하거나 취소할 수 있습니다."))
                .given(reservationService).deleteUserReservation(id, name);

        // when & then
        mockMvc.perform(delete("/reservations/{id}", id)
                        .param("name", name))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("FORBIDDEN_RESERVATION"))
                .andExpect(jsonPath("$.detail").value("본인의 예약만 변경하거나 취소할 수 있습니다."));
    }

    @Test
    void 유효하지_않은_입력값이면_에러_응답() throws Exception {
        // given
        String request = """
                {
                  "name": "",
                  "date": "2099-01-01",
                  "timeId": 1,
                  "themeId": 1
                }
                """;

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.detail").value("name은 비어 있을 수 없습니다."));
    }

    @Test
    void 요청_본문_형식이_올바르지_않으면_에러_응답() throws Exception {
        // given
        String request = """
                {
                  "name": "브라운",
                  "date": "2099-01-01",
                  "timeId": "abc",
                  "themeId": 1
                }
                """;

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.detail").value("요청 본문 형식이 올바르지 않습니다."));
    }

    @Test
    void 지난_예약이면_에러_응답() throws Exception {
        // given
        given(reservationService.createUserReservation(
                eq("브라운"),
                eq(LocalDate.of(2099, 1, 1)),
                eq(1L),
                eq(1L)))
                .willThrow(new PastReservationException("이미 지난 시간으로는 예약할 수 없습니다."));

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("PAST_RESERVATION"))
                .andExpect(jsonPath("$.detail").value("이미 지난 시간으로는 예약할 수 없습니다."));
    }

    @Test
    void 존재하지_않는_리소스이면_에러_응답() throws Exception {
        // given
        given(reservationService.createUserReservation(
                eq("브라운"),
                eq(LocalDate.of(2099, 1, 1)),
                eq(1L),
                eq(1L)))
                .willThrow(new NotFoundException("존재하지 않는 테마입니다."));

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.detail").value("존재하지 않는 테마입니다."));
    }

    @Test
    void 중복_예약이면_에러_응답() throws Exception {
        // given
        given(reservationService.createUserReservation(
                eq("브라운"),
                eq(LocalDate.of(2099, 1, 1)),
                eq(1L),
                eq(1L)))
                .willThrow(new DuplicateReservationException("이미 예약된 시간입니다."));

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE_RESERVATION"))
                .andExpect(jsonPath("$.detail").value("이미 예약된 시간입니다."));
    }

    @Test
    void 예상하지_못한_예외면_서버_에러_응답() throws Exception {
        // given
        given(reservationService.createUserReservation(
                eq("브라운"),
                eq(LocalDate.of(2099, 1, 1)),
                eq(1L),
                eq(1L)))
                .willThrow(new RuntimeException("unexpected"));

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.detail").value("서버에 문제가 발생했습니다."));
    }

    private String validRequest() {
        return """
                {
                  "name": "브라운",
                  "date": "2099-01-01",
                  "timeId": 1,
                  "themeId": 1
                }
                """;
    }

    private Reservation reservation() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "테마", "설명", "썸네일");
        return new Reservation(1L, "브라운", LocalDate.of(2099, 1, 1), time, theme);
    }
}
