package roomescape.controller.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.common.exception.NotFoundException;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;
import roomescape.dto.response.AvailableTimeResponseDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.service.ThemeService;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest {

    private final Theme theme = new Theme(1L, new Name("방탈출테마"), "http://example.com/img.jpg", "방탈출 테마 설명");

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Nested
    class Get {

        static Stream<Arguments> invalidPopularThemeParams() {
            return Stream.of(
                    Arguments.of("limit이 최대값 초과", 16, 7),
                    Arguments.of("days가 최대값 초과", 10, 11),
                    Arguments.of("limit이 0 이하", 0, 7)
            );
        }

        @Test
        @DisplayName("전체 테마 목록을 조회하면 200을 반환한다")
        void returnsAllThemes() {
            given(themeService.findAll()).willReturn(List.of(theme));
            List<ThemeResponseDto> expected = List.of(ThemeResponseDto.from(theme));

            List<ThemeResponseDto> actual = RestAssuredMockMvc.given()
                    .when().get("/themes")
                    .then()
                    .status(HttpStatus.OK)
                    .extract().as(new TypeRef<>() {});

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("존재하는 테마 id를 조회하면 200을 반환한다")
        void returnsThemeById() {
            given(themeService.findById(theme.getId())).willReturn(theme);
            ThemeResponseDto expected = ThemeResponseDto.from(theme);

            ThemeResponseDto actual = RestAssuredMockMvc.given()
                    .when().get("/themes/" + theme.getId())
                    .then()
                    .status(HttpStatus.OK)
                    .extract().as(ThemeResponseDto.class);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("존재하지 않는 테마 id를 조회하면 404를 반환한다")
        void returnsNotFoundWhenIdNotExists() {
            given(themeService.findById(999L)).willThrow(new NotFoundException("존재하지 않는 테마입니다."));

            RestAssuredMockMvc.given()
                    .when().get("/themes/999")
                    .then()
                    .status(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("테마의 이용 가능한 시간 목록을 조회하면 200을 반환한다")
        void returnsAvailableTimes() {
            List<AvailableTimeResponseDto> expected = List.of(
                    new AvailableTimeResponseDto(1L, LocalTime.of(13, 0), false),
                    new AvailableTimeResponseDto(2L, LocalTime.of(14, 0), true)
            );
            given(themeService.findAvailableTimesById(eq(theme.getId()), any(LocalDate.class))).willReturn(expected);

            List<AvailableTimeResponseDto> actual = RestAssuredMockMvc.given()
                    .queryParam("localDate", "2026-05-10")
                    .when().get("/themes/" + theme.getId() + "/times")
                    .then()
                    .status(HttpStatus.OK)
                    .extract().as(new TypeRef<>() {});

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("인기 테마 목록을 조회하면 200을 반환한다")
        void returnsPopularThemes() {
            given(themeService.findPopulars(any())).willReturn(List.of(theme));
            List<ThemeResponseDto> expected = List.of(ThemeResponseDto.from(theme));

            List<ThemeResponseDto> actual = RestAssuredMockMvc.given()
                    .queryParam("limit", "10")
                    .queryParam("days", "7")
                    .when().get("/themes/populars")
                    .then()
                    .status(HttpStatus.OK)
                    .extract().as(new TypeRef<>() {});

            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidPopularThemeParams")
        @DisplayName("유효하지 않은 파라미터로 인기 테마를 조회하면 400을 반환한다")
        void returnsValidationErrorWithInvalidParams(String description, int limit, int days) {
            RestAssuredMockMvc.given()
                    .queryParam("limit", limit)
                    .queryParam("days", days)
                    .when().get("/themes/populars")
                    .then()
                    .status(HttpStatus.BAD_REQUEST);
        }
    }
}
