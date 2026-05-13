package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ThemeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("새로운 테마를 생성하면 201 상태코드와 생성된 데이터를 반환한다")
    void addTheme() throws Exception {
        Map<String, String> params = Map.of(
                "name", "공포의 방",
                "description", "아주 무서운 방입니다.",
                "imageUrl", "https://example.com/image.png"
        );

        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.name").value("공포의 방"));
    }

    @Test
    @DisplayName("테마 생성에서 필수값이 누락되면 400 에러를 반환한다")
    void addThemeWithBlankValue() throws Exception {
        Map<String, String> params = Map.of(
                "name", " ", // @NotBlank 위반
                "description", "설명",
                "imageUrl", "이미지"
        );

        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("테마 이름은 반드시 포함되어야 합니다."));
    }

    @Test
    @DisplayName("테마를 삭제하면 204 상태코드를 반환한다")
    void deleteTheme() throws Exception {
        long id = createThemeAndGetId("삭제용 테마");

        mockMvc.perform(delete("/themes/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("예약에서 사용 중인 테마를 삭제하려고 하면 409 에러를 반환한다")
    void deleteThemeInUse() throws Exception {
        mockMvc.perform(delete("/themes/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("해당 테마를 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다."));
    }

    @Test
    @DisplayName("모든 테마 목록을 조회하면 200 상태코드를 반환한다")
    void getThemes() throws Exception {
        mockMvc.perform(get("/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("기간과 개수 조건에 맞는 인기 테마 목록을 반환한다")
    void getPopularThemes() throws Exception {
        mockMvc.perform(get("/themes/popular")
                        .param("startDate", "2026-05-01")
                        .param("endDate", "2026-05-31")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("인기 테마 조회에서 날짜 형식이 올바르지 않으면 400 에러를 반환한다")
    void getPopularThemesWithInvalidDate() throws Exception {
        mockMvc.perform(get("/themes/popular")
                        .param("startDate", "26/05/01") // 잘못된 형식
                        .param("endDate", "2026-05-31")
                        .param("size", "5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("날짜는 YYYY-MM-DD 형식이여야 합니다."));
    }

    private long createThemeAndGetId(String name) throws Exception {
        Map<String, String> params = Map.of(
                "name", name,
                "description", "설명",
                "imageUrl", "이미지"
        );
        MvcResult result = mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("data").get("id").asLong();
    }
}