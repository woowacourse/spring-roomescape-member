package roomescape.theme.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.AdminThemeService;

@WebMvcTest(AdminThemeController.class)
class AdminThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminThemeService adminThemeService;

    @Test
    void 테마를_생성할_수_있다() throws Exception {
        ThemeRequest request = new ThemeRequest("Theme A", "desc", "https://example.com/thumb.png");
        Theme saved = new Theme(1L, "Theme A", "desc", "https://example.com/thumb.png");

        when(adminThemeService.save(eq("Theme A"), eq("desc"), eq("https://example.com/thumb.png"))).thenReturn(saved);

        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Theme A"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.thumbnail").value("https://example.com/thumb.png"));
    }

    @Test
    void 테마를_삭제할_수_있다() throws Exception {
        mockMvc.perform(delete("/admin/themes/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
