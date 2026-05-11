package roomescape.theme.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.config.TestFixture.themeRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.service.ThemeService;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ThemeService themeService;

    @Test
    void 테마_목록을_조회한다() throws Exception {
        themeService.save(themeRequest("공포의 저택"));

        mockMvc.perform(get("/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("공포의 저택")))
                .andExpect(jsonPath("$[*].description", hasItem("공포의 저택 설명")))
                .andExpect(jsonPath("$[*].thumbnailUrl", hasItem("https://example.com/theme.png")))
                .andExpect(jsonPath("$[*].runtime", hasItem(60)));
    }

    @Sql("/create_dummies_for_popular_themes.sql")
    @Test
    void 최근_1주_동안_예약이_많은_테마_상위_10개를_조회한다() throws Exception {
        mockMvc.perform(get("/themes/popular")
                        .queryParam("days", "7")
                        .queryParam("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].name").value("테마5"))
                .andExpect(jsonPath("$[1].name").value("테마4"))
                .andExpect(jsonPath("$[2].name").value("테마3"))
                .andExpect(jsonPath("$[3].name").value("테마2"))
                .andExpect(jsonPath("$[4].name").value("테마1"));
    }

}
