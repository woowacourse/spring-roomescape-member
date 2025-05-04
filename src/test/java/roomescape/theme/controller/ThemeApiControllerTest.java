package roomescape.theme.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.theme.service.ThemeService;

@WebMvcTest(ThemeApiController.class)
class ThemeApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeService themeService;

    private static final String URI = "/themes";

    @DisplayName("테마 내역을 모두 조회한다")
    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("기간에 따라 테마 순위권을 모두 조회한다")
    @Test
    void findRankedByPeriod() throws Exception {
        mockMvc.perform(get(URI + "/ranked")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("새로운 테마 내역을 추가한다")
    @Test
    void add() throws Exception {
        String requestBody = """
                {
                    "name" : "micky",
                    "description" : "horror",
                    "thumbnail" : "picture"
                }
                """;

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @DisplayName("존재하지 않는 테마 내역 아이디를 삭제하는 경우 예외가 발생한다")
    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete(URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
