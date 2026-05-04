package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import roomescape.domain.Theme;
import roomescape.dto.theme.ThemeRequestDto;
import roomescape.dto.theme.ThemeResponseDto;
import roomescape.service.ThemeService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ThemeControllerTest {

    private static final Theme THEME = new Theme(null, "name", "description", "image-url");
    private static final Theme SAVED_THEME = new Theme(1L, "name", "description", "image-url");
    private static final ThemeRequestDto THEME_REQUEST = ThemeRequestDto.from(THEME);
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
    void 테마를_추가한다() {
        // given
        when(themeService.addTheme(THEME_REQUEST))
                .thenReturn(SAVED_THEME);

        // when
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(THEME_REQUEST)
                .when().post("/themes");

        // then
        response
                .then()
                .statusCode(HttpStatus.CREATED.value());

        ThemeResponseDto responseDto = response.as(ThemeResponseDto.class);
        assertThat(responseDto.id()).isEqualTo(SAVED_THEME.getId());
    }

    @Test
    void 테마를_삭제한다() {
        // given & when
        Response response = RestAssured
                .given().log().all()
                .pathParam("id", 1)
                .when().delete("/themes/{id}");

        // then
        response
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 모든_테마를_조회한다() {
        // given
        List<Theme> themes = List.of(THEME.withId(1L), THEME.withId(2L), THEME.withId(3L));
        List<ThemeResponseDto> dtos = themes.stream()
                .map(ThemeResponseDto::from)
                .toList();

        when(themeService.findAll())
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
}