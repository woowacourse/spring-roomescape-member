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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AdminThemeControllerTest {

    private static final String THEME_NAME = "테마";
    private static final String THEME_DESCRIPTION = "테마 설명";
    private static final String THEME_THUMBNAIL_URL = "https://example.com/theme.png";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 테마를_추가한다() throws Exception {
        // given
        Map<String, Object> request = themeRequestBody(THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL_URL);

        // when
        ResultActions result = mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(THEME_NAME))
                .andExpect(jsonPath("$.description").value(THEME_DESCRIPTION))
                .andExpect(jsonPath("$.thumbnailUrl").value(THEME_THUMBNAIL_URL))
                .andExpect(jsonPath("$.runtime").value(60));
    }

    @Test
    void 테마를_삭제한다() throws Exception {
        // given
        Map<String, Object> request = themeRequestBody(THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL_URL);
        int id = postTheme(request);

        // when
        ResultActions result = mockMvc.perform(delete("/admin/themes/{id}", id));

        // then
        result.andExpect(status().isNoContent());
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
