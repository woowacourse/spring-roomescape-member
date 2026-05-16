package roomescape.presentation;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.ThemeService;
import roomescape.entity.Theme;
import roomescape.entity.ThemeSortType;
import roomescape.global.auth.AdminInterceptor;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ConflictException;

@WebMvcTest(ThemeController.class)
@Import({AdminInterceptor.class, ThemeControllerTest.TestWebConfig.class})
class ThemeControllerTest {

    private static final String TEST_THEME_NAME = "테마";
    private static final String TEST_THEME_DESCRIPTION = "테마 설명";
    private static final String TEST_THEME_THUMBNAIL = "https://image";
    private static final Long TEST_THEME_ID = 1L;

    @TestConfiguration
    static class TestWebConfig implements WebMvcConfigurer {
        @Autowired
        private AdminInterceptor adminInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(adminInterceptor).addPathPatterns("/**");
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ThemeService themeService;

    /**
     * POST /themes - 테마 생성
     */
    @Test
    @DisplayName("POST /themes - 테마 저장 요청을 서비스에 전달하고 201 응답을 반환한다.")
    void addNewTheme_success() throws Exception {
        // given
        Theme theme = Theme.createWithId(TEST_THEME_ID, TEST_THEME_NAME, TEST_THEME_DESCRIPTION, TEST_THEME_THUMBNAIL);
        given(themeService.save(TEST_THEME_NAME, TEST_THEME_DESCRIPTION, TEST_THEME_THUMBNAIL)).willReturn(theme);

        Map<String, Object> body = new HashMap<>();
        body.put("name", TEST_THEME_NAME);
        body.put("description", TEST_THEME_DESCRIPTION);
        body.put("thumbnail", TEST_THEME_THUMBNAIL);

        // when & then
        mockMvc.perform(post("/themes")
                        .header("Authorization", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/themes/1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TEST_THEME_ID))
                .andExpect(jsonPath("$.name").value(TEST_THEME_NAME))
                .andExpect(jsonPath("$.description").value(TEST_THEME_DESCRIPTION))
                .andExpect(jsonPath("$.thumbnail").value(TEST_THEME_THUMBNAIL));

        then(themeService).should().save(TEST_THEME_NAME, TEST_THEME_DESCRIPTION, TEST_THEME_THUMBNAIL);
    }

    @Test
    @DisplayName("POST /themes - 관리자 권한 없이 테마 저장 요청 시 403 에러를 반환한다.")
    void addNewTheme_fail_without_admin() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("name", TEST_THEME_NAME);
        body.put("description", TEST_THEME_DESCRIPTION);
        body.put("thumbnail", TEST_THEME_THUMBNAIL);

        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /themes - 테마 이름이 누락된 경우 THEME_NAME_EMPTY 에러를 반환한다.")
    void addNewTheme_fail_missing_name() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("description", TEST_THEME_DESCRIPTION);
        body.put("thumbnail", TEST_THEME_THUMBNAIL);

        mockMvc.perform(post("/themes")
                        .header("Authorization", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("테마 이름이 비어있습니다. 이름을 입력해주세요."));
    }

    @Test
    @DisplayName("POST /themes - 테마 설명이 누락된 경우 THEME_DESCRIPTION_EMPTY 에러를 반환한다.")
    void addNewTheme_fail_missing_description() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("name", TEST_THEME_NAME);
        body.put("thumbnail", TEST_THEME_THUMBNAIL);

        mockMvc.perform(post("/themes")
                        .header("Authorization", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("테마 설명이 비어있습니다. 설명을 입력해주세요."));
    }

    @Test
    @DisplayName("POST /themes - 썸네일이 누락된 경우 THEME_THUMBNAIL_EMPTY 에러를 반환한다.")
    void addNewTheme_fail_missing_thumbnail() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("name", TEST_THEME_NAME);
        body.put("description", TEST_THEME_DESCRIPTION);

        mockMvc.perform(post("/themes")
                        .header("Authorization", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("테마 썸네일 주소가 비어있습니다. 주소를 입력해주세요."));
    }

    @Test
    @DisplayName("POST /themes - 문법이 틀린 JSON(Malformed)을 보낸 경우 400 에러를 반환한다.")
    void addNewTheme_fail_malformed_json() throws Exception {
        mockMvc.perform(post("/themes")
                        .header("Authorization", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + TEST_THEME_NAME + "\", }")) // 콤마 오류
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("요청 정보가 올바르지 않습니다. 입력 내용을 다시 확인해주세요."));
    }

    @Test
    @DisplayName("GET /themes - 테마 목록 조회 요청을 서비스에 전달하고 200 응답을 반환한다.")
    void readThemes_no_params_success() throws Exception {
        // given
        given(themeService.findAll()).willReturn(
                List.of(Theme.createWithId(TEST_THEME_ID, TEST_THEME_NAME, TEST_THEME_DESCRIPTION, TEST_THEME_THUMBNAIL))
        );

        // when & then
        mockMvc.perform(get("/themes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(TEST_THEME_NAME))
                .andExpect(jsonPath("$[0].description").value(TEST_THEME_DESCRIPTION))
                .andExpect(jsonPath("$[0].thumbnail").value(TEST_THEME_THUMBNAIL));

        then(themeService).should().findAll();
    }

    @Test
    @DisplayName("GET /themes?sortType=... - 기간 기준 인기 테마 조회 요청을 서비스에 전달한다.")
    void readThemes_top_n_by_period_success() throws Exception {
        // given
        LocalDate from = LocalDate.of(2026, 5, 1);
        LocalDate to = LocalDate.of(2026, 5, 31);
        given(themeService.findTopNByPeriod(from, to, ThemeSortType.POPULAR, 2L)).willReturn(
                List.of(
                        Theme.createWithId(1L, "테마1", "설명1", "URL1"),
                        Theme.createWithId(2L, "테마2", "설명2", "URL2")
                )
        );

        // when & then
        mockMvc.perform(get("/themes")
                        .param("sortType", "POPULAR")
                        .param("from", "2026-05-01")
                        .param("to", "2026-05-31")
                        .param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("테마1"))
                .andExpect(jsonPath("$[1].name").value("테마2"));

        then(themeService).should().findTopNByPeriod(from, to, ThemeSortType.POPULAR, 2L);
    }

    @Test
    @DisplayName("GET /themes?sortType=... - 잘못된 정렬 기준을 보낸 경우 400 에러를 반환한다.")
    void readThemes_fail_invalid_sortType() throws Exception {
        mockMvc.perform(get("/themes")
                        .param("sortType", "INVALID")
                        .param("from", "2026-05-01")
                        .param("to", "2026-05-31")
                        .param("limit", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("요청 정보가 올바르지 않습니다. 입력 내용을 다시 확인해주세요."));
    }

    @Test
    @DisplayName("DELETE /themes/{id} - 테마 삭제 요청을 서비스에 전달하고 204 응답을 반환한다.")
    void deleteTheme_success() throws Exception {
        // when & then
        mockMvc.perform(delete("/themes/1")
                        .header("Authorization", "ADMIN"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(themeService).should().deleteById(1L);
    }

    @Test
    @DisplayName("DELETE /themes/{id} - 관리자 권한 없이 테마 삭제 요청 시 403 에러를 반환한다.")
    void deleteTheme_fail_without_admin() throws Exception {
        mockMvc.perform(delete("/themes/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /themes/{id} - 사용 중인 테마를 삭제하려 할 때 409 에러를 반환한다.")
    void deleteTheme_fail_already_used() throws Exception {
        willThrow(new ConflictException(ErrorCode.THEME_ALREADY_USED))
                .given(themeService).deleteById(anyLong());

        mockMvc.perform(delete("/themes/1")
                        .header("Authorization", "ADMIN"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("사용 중인 테마는 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요."));
    }
}
