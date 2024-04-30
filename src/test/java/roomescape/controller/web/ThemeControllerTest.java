package roomescape.controller.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.dto.theme.ThemeCreateRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.service.ThemeService;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ThemeService themeService;

    @Test
    @DisplayName("전체 테마를 조회한다.")
    void readAll() throws Exception {
        //given
        List<ThemeResponse> responses = List.of(
                ThemeResponse.of(1L, "방탈출1", "1번 방탈출", "썸네일 1"),
                ThemeResponse.of(2L, "방탈출2", "2번 방탈출", "썸네일 2")
        );
        given(themeService.findAll())
                .willReturn(responses);

        //when //then
        mockMvc.perform(get("/themes"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void create() throws Exception {
        //given
        ThemeCreateRequest givenRequest = ThemeCreateRequest.of( "방탈출1", "1번 방탈출", "썸네일 1");
        ThemeResponse response = ThemeResponse.of(1L, "방탈출1", "1번 방탈출", "썸네일 1");
        given(themeService.add(givenRequest)).willReturn(response);
        String requestBody = objectMapper.writeValueAsString(givenRequest);

        //when //then
        mockMvc.perform(post("/themes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
