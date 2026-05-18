package roomescape.admin.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.global.exception.ErrorCode;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.ReservationService;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ThemeService themeService;

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    @MockitoBean
    private ReservationService reservationService;

    @InjectMocks
    private AdminController adminController;

    @Test
    void 시간_생성_요청을_받으면_DTO의_시작_시간을_Service에_전달하고_결과를_반환한다() throws Exception {
        ReservationTime created = new ReservationTime(1L, LocalTime.of(18, 0));

        when(reservationTimeService.createTime(any())).thenReturn(created);

        mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "startAt": "18:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.startAt").value("18:00"));
    }

    @Test
    void 시간_삭제_요청을_받으면_PathVariable_id를_Service에_전달한다() throws Exception {
        mockMvc.perform(delete("/admin/times/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void 테마_생성_요청을_받으면_DTO의_테마명_설명_이미지주소를_Service에_전달하고_결과를_반환한다() throws Exception {
        Theme created = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        when(themeService.createTheme(any(), any(), any())).thenReturn(created);
        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {
                                    "name": "공포방",
                                    "description": "무서운방입니다.",
                                    "thumbnail": "image-url"
                                  }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("공포방"))
                .andExpect(jsonPath("$.description").value("무서운방입니다."))
                .andExpect(jsonPath("$.thumbnail").value("image-url"));

    }

    @Test
    void 테마_삭제_요청을_받으면_PathVariable_id를_Service에_전달한다() throws Exception {
        mockMvc.perform(delete("/admin/themes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void 테마요청DTO_테마명이_빈_문자열이면_400과_INVALID_INPUT_에러를_반환한다() throws Exception {
        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "description": "사이클2미션방입니다.",
                                  "thumbnail": "/path/to/cycle2.img"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT.errorCode()))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("테마명은 필수입니다."));
    }

    @Test
    void 테마생성요청DTO_모든_필드가_동시에_잘못되면_모든_필드_에러가_담긴다() throws Exception {
        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": null,
                                  "description": null,
                                  "thumbnail": null
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT.errorCode()))
                .andExpect(jsonPath("$.fieldErrors.length()").value(3));
    }

    @Test
    void 예약시간생성요청DTO_시간이_빈값이면_INVALID_INPUT_에러를_반환한다() throws Exception {
        mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "startAt" : ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT.errorCode()))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("startAt"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("예약 시작 시간은 필수입니다."));
    }

    @Test
    void 예약_목록_조회_요청을_Service에_전달하고_결과를_반환한다() throws Exception {
        List<Reservation> reservations = List.of(
                new Reservation(1L, "레서",
                        LocalDate.of(2026, 5, 6),
                        new ReservationTime(1L, LocalTime.of(18, 0)),
                        new Theme(1L, "공포방", "무서운방입니다.", "image-url")),
                new Reservation(2L, "어셔",
                        LocalDate.of(2026, 5, 7),
                        new ReservationTime(2L, LocalTime.of(20, 0)),
                        new Theme(2L, "추리방", "추리하는방입니다.", "image-url2"))
        );
        when(reservationService.getReservations()).thenReturn(reservations);

        mockMvc.perform(get("/admin/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("레서"))
                .andExpect(jsonPath("$[0].date").value("2026-05-06"))
                .andExpect(jsonPath("$[0].time.startAt").value("18:00"))
                .andExpect(jsonPath("$[0].theme.name").value("공포방"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("어셔"));
    }
}
