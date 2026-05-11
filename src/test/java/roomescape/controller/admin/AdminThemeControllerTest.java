package roomescape.controller.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.fixture.ThemeRequestDtoFixture;
import roomescape.service.ThemeService;

@WebMvcTest(AdminThemeController.class)
class AdminThemeControllerTest {

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

        @Test
        @DisplayName("전체 테마 목록을 조회하면 200을 반환한다")
        void returnsAllThemes() {
            given(themeService.findAll()).willReturn(List.of(theme));
            List<ThemeResponseDto> expected = List.of(ThemeResponseDto.from(theme));

            List<ThemeResponseDto> actual = RestAssuredMockMvc.given()
                    .when().get("/admin/themes")
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
                    .when().get("/admin/themes/" + theme.getId())
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
                    .when().get("/admin/themes/999")
                    .then()
                    .status(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class Post {

        static Stream<Arguments> invalidThemeRequests() {
            return Stream.of(
                    Arguments.of("name이 공백", ThemeRequestDtoFixture.withBlankName()),
                    Arguments.of("name이 40자 초과", ThemeRequestDtoFixture.withNameExceedingMaxLength()),
                    Arguments.of("thumbnailUrl이 URL 형식이 아님", ThemeRequestDtoFixture.withInvalidThumbnailUrl()),
                    Arguments.of("description이 200자 초과", ThemeRequestDtoFixture.withDescriptionExceedingMaxLength())
            );
        }

        @Test
        @DisplayName("유효한 요청으로 테마를 생성하면 201을 반환한다")
        void createsTheme() {
            ThemeRequestDto requestDto = new ThemeRequestDto(
                    theme.getName().getValue(), theme.getThumbnailUrl(), theme.getDescription());
            given(themeService.create(any())).willReturn(theme);
            ThemeResponseDto expected = ThemeResponseDto.from(theme);

            ThemeResponseDto actual = RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/admin/themes")
                    .then()
                    .status(HttpStatus.CREATED)
                    .header("Location", "http://localhost/admin/themes/" + theme.getId())
                    .extract().as(ThemeResponseDto.class);

            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidThemeRequests")
        @DisplayName("유효하지 않은 요청으로 테마를 생성하면 400을 반환한다")
        void returnsValidationError(String description, ThemeRequestDto invalidRequest) {
            RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(invalidRequest)
                    .when().post("/admin/themes")
                    .then()
                    .status(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("중복된 테마를 생성하면 409를 반환한다")
        void returnsConflictWhenDuplicateTheme() {
            ThemeRequestDto requestDto = new ThemeRequestDto(
                    theme.getName().getValue(), theme.getThumbnailUrl(), theme.getDescription());
            given(themeService.create(any())).willThrow(new ConflictException("이미 존재하는 테마명입니다."));

            RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/admin/themes")
                    .then()
                    .status(HttpStatus.CONFLICT);
        }
    }

    @Nested
    class Delete {

        @Test
        @DisplayName("테마를 삭제하면 204를 반환한다")
        void deletesTheme() {
            willDoNothing().given(themeService).delete(theme.getId());

            RestAssuredMockMvc.given()
                    .when().delete("/admin/themes/" + theme.getId())
                    .then()
                    .status(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("예약이 있는 테마를 삭제하면 409를 반환한다")
        void returnsConflictWhenThemeHasReservation() {
            willThrow(new ConflictException("해당 테마에 예약이 존재합니다.")).given(themeService).delete(theme.getId());

            RestAssuredMockMvc.given()
                    .when().delete("/admin/themes/" + theme.getId())
                    .then()
                    .status(HttpStatus.CONFLICT);
        }
    }
}
