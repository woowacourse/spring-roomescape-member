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
    class GET {

        @Test
        void 전체_테마_목록을_조회하면_200을_반환한다() {
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
        void 존재하는_테마_id를_조회하면_200을_반환한다() {
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
        void 존재하지_않는_테마_id를_조회하면_404를_반환한다() {
            given(themeService.findById(999L)).willThrow(new NotFoundException("존재하지 않는 테마입니다."));

            RestAssuredMockMvc.given()
                    .when().get("/admin/themes/999")
                    .then()
                    .status(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class POST {

        static Stream<Arguments> 유효하지_않은_테마_요청_목록() {
            return Stream.of(
                    Arguments.of("name이 공백", ThemeRequestDtoFixture.withBlankName()),
                    Arguments.of("name이 40자 초과", ThemeRequestDtoFixture.withNameExceedingMaxLength()),
                    Arguments.of("thumbnailUrl이 URL 형식이 아님", ThemeRequestDtoFixture.withInvalidThumbnailUrl()),
                    Arguments.of("description이 200자 초과", ThemeRequestDtoFixture.withDescriptionExceedingMaxLength())
            );
        }

        @Test
        void 유효한_요청으로_테마를_생성하면_201을_반환한다() {
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
        @MethodSource("유효하지_않은_테마_요청_목록")
        void 유효하지_않은_요청으로_테마를_생성하면_400을_반환한다(String description, ThemeRequestDto invalidRequest) {
            RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(invalidRequest)
                    .when().post("/admin/themes")
                    .then()
                    .status(HttpStatus.BAD_REQUEST);
        }

        @Test
        void 중복된_테마를_생성하면_409를_반환한다() {
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
    class DELETE {

        @Test
        void 테마를_삭제하면_204를_반환한다() {
            willDoNothing().given(themeService).delete(theme.getId());

            RestAssuredMockMvc.given()
                    .when().delete("/admin/themes/" + theme.getId())
                    .then()
                    .status(HttpStatus.NO_CONTENT);
        }

        @Test
        void 예약이_있는_테마를_삭제하면_409를_반환한다() {
            willThrow(new ConflictException("해당 테마에 예약이 존재합니다.")).given(themeService).delete(theme.getId());

            RestAssuredMockMvc.given()
                    .when().delete("/admin/themes/" + theme.getId())
                    .then()
                    .status(HttpStatus.CONFLICT);
        }
    }
}
