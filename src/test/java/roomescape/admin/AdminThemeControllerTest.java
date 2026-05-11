package roomescape.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.theme.ThemeService;
import roomescape.theme.dto.ThemeResponse;

@WebMvcTest(AdminThemeController.class)
class AdminThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ThemeService themeService;

    @Test
    void 테마_추가() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("name", "재밌는방탈출");
        params.put("description", "재밌는방탈출");
        params.put("thumbnail", "s3.com");

        given(themeService.create(any()))
                .willReturn(new ThemeResponse(6L, "재밌는방탈출", "재밌는방탈출", "s3.com"));

        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isCreated());
    }

    @Test
    void 테마_삭제() throws Exception {
        willDoNothing().given(themeService).delete(6L);

        mockMvc.perform(delete("/admin/themes/6"))
                .andExpect(status().isNoContent());
    }

    @Test
    void 빈값으로_테마_추가시_400() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("description", "재밌는방탈출");
        params.put("thumbnail", "s3.com");

        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 존재하지_않는_테마_삭제시_404() throws Exception {
        willThrow(new RoomescapeException(ErrorCode.THEME_NOT_FOUND))
                .given(themeService).delete(0L);

        mockMvc.perform(delete("/admin/themes/0"))
                .andExpect(status().isNotFound());
    }
}
