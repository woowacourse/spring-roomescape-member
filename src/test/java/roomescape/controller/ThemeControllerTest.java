package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.config.AuthenticationExtractor;
import roomescape.domain.member.Member;
import roomescape.dto.theme.ThemeCreateRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.fixture.MemberFixtures;
import roomescape.service.AuthService;
import roomescape.service.ThemeService;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ThemeService themeService;
    @MockBean
    private AuthService authService;
    @MockBean
    private AuthenticationExtractor authenticationExtractor;

    @BeforeEach
    void setUp() {
        Member member = MemberFixtures.createUserMember("daon");
        given(authService.findAuthInfo(anyString())).willReturn(member);
        given(authenticationExtractor.extractAuthInfo(any(HttpServletRequest.class))).willReturn(member);
    }

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
        ThemeCreateRequest givenRequest = ThemeCreateRequest.of("방탈출1", "1번 방탈출", "썸네일 1");
        ThemeResponse response = ThemeResponse.of(1L, "방탈출1", "1번 방탈출", "썸네일 1");
        given(themeService.add(any(ThemeCreateRequest.class))).willReturn(response);
        String requestBody = objectMapper.writeValueAsString(givenRequest);

        //when //then
        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteWithId() throws Exception {
        //given
        long givenId = 1L;

        //when //then
        mockMvc.perform(delete("/themes/{id}", givenId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("유효하지않는 값이 입력되면 Bad Request 응답을 반환한다.")
    void createThemeByInvalidRequest() throws Exception {
        //given
        ThemeCreateRequest givenRequest
                = ThemeCreateRequest.of("InvalidName", "InvalidDescription", "InvalidThumbnail");
        given(themeService.add(any(ThemeCreateRequest.class)))
                .willThrow(IllegalArgumentException.class);
        String requestBody = objectMapper.writeValueAsString(givenRequest);

        //when //then
        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
