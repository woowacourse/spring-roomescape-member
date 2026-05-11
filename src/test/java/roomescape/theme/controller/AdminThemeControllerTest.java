package roomescape.theme.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.config.TestFixture.themeRequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AdminThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 테마를_추가한다() throws Exception {
        Map<String, Object> request = themeRequestBody("테마", "테마 설명", "https://example.com/theme.png");

        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("테마"))
                .andExpect(jsonPath("$.description").value("테마 설명"))
                .andExpect(jsonPath("$.thumbnailUrl").value("https://example.com/theme.png"))
                .andExpect(jsonPath("$.runtime").value(60));
    }

    @Test
    void 테마를_삭제한다() throws Exception {
        Map<String, Object> request = themeRequestBody("테마", "테마 설명", "https://example.com/theme.png");
        int id = postTheme(request);

        mockMvc.perform(delete("/admin/themes/{id}", id))
                .andExpect(status().isNoContent());
    }

    private int postTheme(Map<String, Object> request) throws Exception {
        MvcResult result = mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id")
                .asInt();
    }

}
