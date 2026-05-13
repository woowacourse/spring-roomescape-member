package roomescape.reservation.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.ReservationService;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void 예약_생성_요청을_받으면_DTO의_이름_날짜_시간_id_테마_id를_Service에_전달하고_결과를_반환한다() throws Exception {
        Reservation created = new Reservation(1L, "레서", LocalDate.of(2026, 5, 6),
                new ReservationTime(1L, LocalTime.of(18,0)),
                new Theme(1L, "공포방", "무서운방입니다.", "image-url"));

        when(reservationService.createReservation(any(), any(), any(), any())).thenReturn(created);
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {
                                    "name": "레서",
                                    "date": "2026-05-06",
                                    "timeId": 1,
                                    "themeId": 1
                                  }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("레서"))
                .andExpect(jsonPath("$.date").value("2026-05-06"))
                .andExpect(jsonPath("$.time.id").value(1))
                .andExpect(jsonPath("$.time.startAt").value("18:00"))
                .andExpect(jsonPath("$.theme.id").value(1))
                .andExpect(jsonPath("$.theme.name").value("공포방"))
                .andExpect(jsonPath("$.theme.description").value("무서운방입니다."))
                .andExpect(jsonPath("$.theme.thumbnail").value("image-url"));
    }

    @Test
    void 예약_삭제_요청을_받으면_PathVariable_id를_Service에_전달한다() throws Exception {
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());
        verify(reservationService, times(1)).deleteReservation(any());
    }

    @Test
    void 중복_예약_요청_시_DuplicateReservationException이_발생하면_400을_반환한다() throws Exception {
        when(reservationService.createReservation(any(), any(), any(), any()))
                .thenThrow(new BusinessException(ErrorCode.RESERVATION_DUPLICATE));

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {
                                    "name": "어셔",
                                    "date": "2026-05-10",
                                    "timeId": 1,
                                    "themeId": 1
                                  }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.RESERVATION_DUPLICATE.errorCode()));
    }

    @Test
    void 예약자_이름이_빈_문자열이면_400과_INVALID_INPUT을_반환한다() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "", 
                                    "date": "2026-05-13", 
                                    "timeId": "1",
                                    "themeId": "1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT.errorCode()))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("예약자 이름은 필수입니다."));
    }

    @Test
    void 예약_날짜가_null이면_400과_INVALID_INPUT을_반환한다() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "어셔",
                              "date": null,
                              "timeId": 1,
                              "themeId": 1
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT.errorCode()))
                .andExpect(jsonPath("$.fieldErrors[?(@.field == 'date')].message")
                        .value(hasItem("예약 날짜는 필수입니다.")));
    }

    @Test
    void 여러_필드가_동시에_잘못되면_모든_필드_에러가_담긴다() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "",
                              "date": null,
                              "timeId": null,
                              "themeId": null
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT.errorCode()))
                .andExpect(jsonPath("$.fieldErrors.length()").value(4));
    }

    @Test
    void 사용자_이름으로_예약_조회_요청을_받으면_해당_사용자의_예약을_반환한다() throws Exception {
        List<Reservation> reservations = List.of(
                new Reservation(1L, "어셔",
                        LocalDate.of(2026, 5, 6),
                        new ReservationTime(1L, LocalTime.of(18, 0)),
                        new Theme(1L, "공포방", "무서운방입니다.", "image-url")),
                new Reservation(2L, "어셔",
                        LocalDate.of(2026, 5, 8),
                        new ReservationTime(2L, LocalTime.of(20, 0)),
                        new Theme(2L, "추리방", "추리하는방입니다.", "image-url2"))
        );
        when(reservationService.getUserReservations("어셔")).thenReturn(reservations);

        mockMvc.perform(get("/reservations").param("name", "어셔"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("어셔"))
                .andExpect(jsonPath("$[0].date").value("2026-05-06"))
                .andExpect(jsonPath("$[0].time.startAt").value("18:00"))
                .andExpect(jsonPath("$[0].theme.name").value("공포방"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].date").value("2026-05-08"));
    }

    @Test
    void 사용자_이름에_해당하는_예약이_없으면_빈_배열을_반환한다() throws Exception {
        when(reservationService.getUserReservations("어셔")).thenReturn(List.of());

        mockMvc.perform(get("/reservations").param("name", "어셔"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void 쿼리_파라미터_name이_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isBadRequest());
    }
}
