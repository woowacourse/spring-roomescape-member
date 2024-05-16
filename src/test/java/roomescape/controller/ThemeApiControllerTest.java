package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.Theme;
import roomescape.service.LoginService;
import roomescape.service.ThemeService;
import roomescape.service.dto.ThemeRequest;
import roomescape.service.dto.ThemeResponse;

@WebMvcTest(ThemeApiController.class)
class ThemeApiControllerTest {

    @MockBean
    private ThemeService themeServiceService;

    @MockBean
    private LoginService loginService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("/themes GET 요청 시 모든 테마 정보와 200 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_and_saved_all_themes_when_get_request() throws Exception {
        List<ThemeResponse> responseDtos = List.of(
                new ThemeResponse(new Theme(1L, "이름", "내용", "썸네일")),
                new ThemeResponse(new Theme(2L, "아토", "방탈출", "좋아.jpg"))
        );

        given(themeServiceService.findAllThemes()).willReturn(responseDtos);

        mockMvc.perform(get("/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @DisplayName("/themes POST 요청 시 저장된 테마와 201 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_and_saved_theme_when_post_request() throws Exception {
        ThemeRequest requestDto = new ThemeRequest("안돌", "안녕하세요", "hello.png");
        String requestBody = objectMapper.writeValueAsString(requestDto);

        ThemeResponse responseDto = new ThemeResponse(new Theme(1L, "안돌", "안녕하세요", "hello.png"));

        given(themeServiceService.createTheme(any())).willReturn(responseDto);

        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("안돌")))
                .andExpect(jsonPath("$.description", is("안녕하세요")))
                .andExpect(jsonPath("$.thumbnail", is("hello.png")));
    }

    @DisplayName("/themes/{id} DELETE 요청 시 204 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_when_delete_request() throws Exception {
        mockMvc.perform(delete("/themes/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
