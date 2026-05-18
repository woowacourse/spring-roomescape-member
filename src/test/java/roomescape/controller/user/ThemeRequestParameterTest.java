package roomescape.controller.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.service.ThemeService;

@WebMvcTest(ThemeController.class)
class ThemeRequestParameterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeService themeService;

    @Test
    @DisplayName("예약 가능 시간 조회 시 날짜 쿼리 파라미터가 없으면 에러 응답을 반환한다.")
    void failFindAvailableTimes_WhenDateParameterIsMissing() throws Exception {
        mockMvc.perform(get("/themes/1/times"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.path").value("/themes/1/times"));
    }

    @Test
    @DisplayName("예약 가능 시간 조회 시 날짜 형식이 올바르지 않으면 에러 응답을 반환한다.")
    void failFindAvailableTimes_WhenDateParameterIsInvalid() throws Exception {
        mockMvc.perform(get("/themes/1/times")
                        .queryParam("date", "2026/05/16"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.path").value("/themes/1/times"));
    }

    @Test
    @DisplayName("예약 가능 시간 조회 시 테마 id 형식이 올바르지 않으면 에러 응답을 반환한다.")
    void failFindAvailableTimes_WhenThemeIdIsInvalid() throws Exception {
        mockMvc.perform(get("/themes/abc/times")
                        .queryParam("date", "2026-05-16"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.path").value("/themes/abc/times"));
    }

    @Test
    @DisplayName("존재하지 않는 API 경로로 요청하면 에러 응답을 반환한다.")
    void failRequest_WhenApiPathDoesNotExist() throws Exception {
        mockMvc.perform(get("/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("API_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/not-found"));
    }
}
