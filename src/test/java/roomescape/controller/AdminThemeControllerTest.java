package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.Theme;
import roomescape.dto.theme.CreateThemeRequest;
import roomescape.service.ThemeService;

@WebMvcTest(AdminThemeController.class)
class AdminThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ThemeService themeService;

    @Test
    void POST_admin_themes_생성된_id를_Location_헤더에_담아_201을_반환한다() throws Exception {
        given(themeService.createTheme(any(CreateThemeRequest.class)))
                .willReturn(new Theme(7L, "공포", "무서움", "https://thumbnail.url"));

        Map<String, Object> body = Map.of(
                "name", "공포",
                "description", "무서움",
                "thumbnailImageUrl", "https://thumbnail.url");

        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/themes/7"));
    }

    @Test
    void DELETE_admin_themes_id_200을_반환하고_서비스에_위임한다() throws Exception {
        mockMvc.perform(delete("/admin/themes/3"))
                .andExpect(status().isOk());

        verify(themeService).deleteTheme(3L);
    }
}