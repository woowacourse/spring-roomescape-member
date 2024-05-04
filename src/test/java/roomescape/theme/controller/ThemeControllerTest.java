package roomescape.theme.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.theme.dto.request.CreateThemeRequest;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class createTheme {

        @Test
        @DisplayName("예약 테마 생성 요청 시 201 상태와 Location 헤더에 생성된 리소스의 위치를 반환한다.")
        void createTheme() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/themes")
                            .content(objectMapper.writeValueAsString(new CreateThemeRequest("마크", "도망갔다.", "https://abc.com")))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().stringValues("Location", "/themes/11"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약 테마 생성 요청 시 테마명이 공백인 경우 404를 반환한다.")
        void createTheme_WhenThemeNameInNull(String name) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/themes")
                            .content(objectMapper.writeValueAsString(new CreateThemeRequest(name, "도망갔다.", "https://abc.com")))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$").value("테마 명은 공백 문자가 불가능합니다.")
                    );
        }

        @Test
        @DisplayName("예약 테마 생성 요청 시 테마명이 255자를 초과했을 경우 400를 반환한다.")
        void createTheme_WhenThemeNameInOverLength() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/themes")
                            .content(objectMapper.writeValueAsString(new CreateThemeRequest("a".repeat(256), "도망갔다.", "https://abc.com")))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$").value("테마명은 최대 255자까지 입력이 가능합니다.")
                    );
        }

        @Test
        @DisplayName("예약 테마 생성 요청 시 테마 설명이 255자를 초과했을 경우 400를 반환한다.")
        void createTheme_WhenDescriptionInOverLength() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/themes")
                            .content(objectMapper.writeValueAsString(new CreateThemeRequest("로코", "a".repeat(256), "https://abc.com")))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$").value("테마 설명은 최대 255자까지 입력이 가능합니다.")
                    );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약 테마 생성 요청 시 테마 설명이 공백인 404를 반환한다.")
        void createTheme_WhenDescriptionInNull(String description) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/themes")
                            .content(objectMapper.writeValueAsString(
                                    new CreateThemeRequest("환상의 나라", description, "https://abc.com")))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$").value("테마 설명은 공백 문자가 불가능합니다.")
                    );
        }

        @Test
        @DisplayName("예약 테마 생성 요청 시 테마 썸네일이 255자를 초과했을 경우 400를 반환한다.")
        void createTheme_WhenThumbnailInOverLength() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/themes")
                            .content(objectMapper.writeValueAsString(new CreateThemeRequest("로코", "달달한 이야기", "a".repeat(256))))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$").value("테마 썸네일은 최대 255자까지 입력이 가능합니다.")
                    );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약 테마 생성 요청 시 테마 썸네일이 공백인 경우 404를 반환한다.")
        void createTheme_WhenThumbnailInNull(String thumbnail) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/themes")
                            .content(objectMapper.writeValueAsString(new CreateThemeRequest("환상의 나라", "동화 속 이야기", thumbnail)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$").value("테마 썸네일은 공백 문자가 불가능합니다.")
                    );
        }
    }

    @Test
    @DisplayName("테마 목록 조회 요청 성공 시 200과 해당 정보를 반환한다.")
    void getThemes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/themes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].name").value("공포"),
                        jsonPath("$[0].description").value("무서워"),
                        jsonPath("$[0].thumbnail").value(
                                "https://zerolotteworld.com/storage/WAYH10LvyaCuAb9ndj1apZIpzEAdpjeAhPR7Gb7J.jpg"),

                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].name").value("액션"),
                        jsonPath("$[1].description").value("신나"),
                        jsonPath("$[1].thumbnail").value("https://sherlock-holmes.co.kr/attach/theme/17000394031.jpg"),

                        status().isOk()
                );
    }

    @Test
    @DisplayName("인기 테마 목록 조회 요청 성공 시 200과 해당 정보를 반환한다.")
    void getPopularThemes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/themes/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].name").value("공포"),
                        jsonPath("$[0].description").value("무서워"),
                        jsonPath("$[0].thumbnail").value(
                                "https://zerolotteworld.com/storage/WAYH10LvyaCuAb9ndj1apZIpzEAdpjeAhPR7Gb7J.jpg"),

                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].name").value("액션"),
                        jsonPath("$[1].description").value("신나"),
                        jsonPath("$[1].thumbnail").value("https://sherlock-holmes.co.kr/attach/theme/17000394031.jpg"),

                        status().isOk()
                );
    }

    @Nested
    class deleteTheme {

        @Test
        @DisplayName("테마 삭제 요청 성공 시 204 상태 코드를 반환한다.")
        void deleteTheme() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/themes/10"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("테마 삭제 요청 시 해당 테마가 존재하지 않는 경우 404 상태 코드를 반환한다.")
        void deleteTheme_isNotFound() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/themes/100"))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$").value("삭제하려는 테마가 존재하지 않습니다.")
                    );
        }

        @Test
        @DisplayName("테마 삭제 요청 시 해당 테마인 예약이 존재할 경우 409 상태 코드를 반환한다.")
        void deleteTheme_isConflict() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/themes/3"))
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$").value("삭제하려는 테마를 사용 중인 예약이 존재합니다.")
                    );
        }
    }
}
