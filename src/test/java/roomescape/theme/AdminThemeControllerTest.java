package roomescape.theme;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminThemeController.class)
class AdminThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminThemeService adminThemeService;

    @Test
    @DisplayName("테마를 생성할 수 있다")
    void createTheme() throws Exception {
        ThemeRequest request = new ThemeRequest("Theme A", "desc", "thumb");
        Theme saved = new Theme(1L, "Theme A", "desc", "thumb");

        when(adminThemeService.save(eq("Theme A"), eq("desc"), eq("thumb"))).thenReturn(saved);

        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Theme A"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.thumbnail").value("thumb"));
    }

    @Test
    @DisplayName("테마를 삭제할 수 있다")
    void deleteTheme() throws Exception {
        mockMvc.perform(delete("/admin/themes/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}

