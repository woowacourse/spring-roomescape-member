package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
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
}