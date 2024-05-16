package roomescape.theme.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.theme.dto.request.CreateThemeRequest;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/delete-data.sql", "/data.sql"})
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTheme() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/themes")
                        .content(objectMapper.writeValueAsString(new CreateThemeRequest("마크", "도망갔다.", "https://abc.com")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "/themes/11"));
    }

    @Test
    void getThemes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/themes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("공포"))
                .andExpect(jsonPath("$[0].description").value("무서워"))
                .andExpect(jsonPath("$[0].thumbnail").value("https://zerolotteworld.com/storage/WAYH10LvyaCuAb9ndj1apZIpzEAdpjeAhPR7Gb7J.jpg"))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("액션"))
                .andExpect(jsonPath("$[1].description").value("신나"))
                .andExpect(jsonPath("$[1].thumbnail").value("https://sherlock-holmes.co.kr/attach/theme/17000394031.jpg"))

                .andExpect(status().isOk());
    }

    @Test
    void getPopularThemes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/themes/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("공포"))
                .andExpect(jsonPath("$[0].description").value("무서워"))
                .andExpect(jsonPath("$[0].thumbnail").value("https://zerolotteworld.com/storage/WAYH10LvyaCuAb9ndj1apZIpzEAdpjeAhPR7Gb7J.jpg"))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("액션"))
                .andExpect(jsonPath("$[1].description").value("신나"))
                .andExpect(jsonPath("$[1].thumbnail").value("https://sherlock-holmes.co.kr/attach/theme/17000394031.jpg"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTheme() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/themes/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTheme_isConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/themes/3"))
                .andExpect(status().isConflict());
    }
}
