package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservationTime.ReservationTimeRequestDto;
import roomescape.dto.reservationTime.ReservationTimeResponseDto;
import roomescape.dto.theme.PopularThemesResponseDto;
import roomescape.dto.theme.ThemeRequestDto;
import roomescape.dto.theme.ThemeResponseDto;
import roomescape.service.ThemeService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ThemeControllerTest {

    private static final Theme THEME = new Theme(null, "name", "description", "image-url");
    private static final Theme SAVED_THEME = new Theme(1L, "name", "description", "image-url");
    private static final ThemeResponseDto THEME_RESPONSE = ThemeResponseDto.from(THEME);

    @LocalServerPort
    private int port;

    @MockitoBean
    private ThemeService themeService;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void 모든_테마를_조회한다() {
        // given
        List<Theme> themes = List.of(THEME.withId(1L), THEME.withId(2L), THEME.withId(3L));
        List<ThemeResponseDto> dtos = themes.stream()
                .map(ThemeResponseDto::from)
                .toList();

        when(themeService.getThemes())
                .thenReturn(themes);

        // when
        Response response = RestAssured
                .given().log().all()
                .when().get("/themes");

        // then
        response
                .then()
                .statusCode(HttpStatus.OK.value());

        List<ThemeResponseDto> responseDtos = response.as(new TypeRef<>() {});
        assertThat(responseDtos).hasSize(3);
        assertThat(responseDtos).containsExactlyElementsOf(dtos);
    }

    @Test
    void 최근_1주일간_예약이_많은_테마_상위_10개를_조회할_수_있다() {
        // given
        List<Theme> tenPopularThemesOrderByRank = createTenThemes();
        PopularThemesResponseDto expectedResponse = PopularThemesResponseDto.from(tenPopularThemesOrderByRank);

        when(themeService.findWeekPopularThemesOrderByRank(10))
            .thenReturn(tenPopularThemesOrderByRank);

        // when
        Response response = RestAssured
            .given().log().all()
            .queryParam("limit", 10)
            .when().get("/themes/popular/week");

        // then
        response
            .then()
            .statusCode(HttpStatus.OK.value());

        PopularThemesResponseDto actualResponse = response.as(new TypeRef<>() {});
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Nested
    @DisplayName("인가 권한 테스트")
    class RoleForbidden {
        @Test
        void 관리자는_테마를_추가할_수_있다() {
            // given
            ThemeRequestDto request = themeRequestDtoFrom(THEME);
            when(themeService.addTheme(any()))
                    .thenReturn(SAVED_THEME);

            // when
            Response response = RestAssured
                    .given().log().all()
                    .queryParam("role", "admin")
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/themes");

            // then
            response
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            ThemeResponseDto responseDto = response.as(ThemeResponseDto.class);
            assertThat(responseDto).isEqualTo(ThemeResponseDto.from(SAVED_THEME));
        }

        @Test
        void 관리자는_테마를_삭제할_수_있다() {
            // given & when
            Response response = RestAssured
                    .given().log().all()
                    .queryParam("role", "admin")
                    .pathParam("id", 1)
                    .when().delete("/themes/{id}");

            // then
            response
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 관리자가_아닌_사용자가_테마를_추가하는_경우_예외가_발생한다() {
            // given
            ThemeRequestDto request = themeRequestDtoFrom(THEME);

            // when
            Response response = RestAssured
                    .given().log().all()
                    .queryParam("role", "user")
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/themes");


            // then
            response
                    .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
        }

        @Test
        void 관리자가_아닌_사용자가_테마를_삭제하는_경우_예외가_발생한다() {
            // given & when
            Response response = RestAssured
                    .given().log().all()
                    .queryParam("role", "user")
                    .pathParam("id", 1)
                    .when().delete("/themes/{id}");

            // then
            response
                    .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
        }
    }

    private List<Theme> createTenThemes() {
        List<Theme> themes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            themes.add(new Theme((long) i, "테마" + i, "테마" + i, "테마" + i));
        }
        return themes;
    }

    private ThemeRequestDto themeRequestDtoFrom(Theme theme) {
        return new ThemeRequestDto(
                theme.getName(),
                theme.getDescription(),
                theme.getImageUrl()
        );
    }
}