package roomescape.admin.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.global.exception.ErrorCode;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
