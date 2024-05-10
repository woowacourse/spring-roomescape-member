package roomescape.theme.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import roomescape.auth.service.AuthService;
import roomescape.theme.dto.ThemeRequestDto;
import roomescape.theme.service.ThemeService;

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


    @DisplayName("invalid한 save 요청이 들어오면 예외가 발생한다.")
    @Test
    void invalidSave() throws Exception {
        ThemeRequestDto requestDto = new ThemeRequestDto(null, "description", "thumbnail");
        mockMvc.perform(post("/themes")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("invalid한 delete 요청이 들어오면 예외가 발생한다.")
    @Test
    void invalidDelete() throws Exception {
        mockMvc.perform(delete("/themes/0"))
                .andExpect(status().isBadRequest());
    }
}
