package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.StandardSoftAssertionsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.dto.request.ReservationTimeSaveRequest;
import roomescape.reservation.dto.request.ThemeSaveRequest;
import roomescape.reservation.dto.response.ReservationTimeResponse;
import roomescape.reservation.dto.response.ThemeResponse;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.THEME_THUMBNAIL;

@Sql("/test-schema.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ApiAcceptanceTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected Long saveTheme(String name, String description) {
        ThemeSaveRequest request = new ThemeSaveRequest(name, description, THEME_THUMBNAIL);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().extract()
                .as(ThemeResponse.class)
                .id();
    }

    protected Long saveReservationTime(LocalTime time) {
        ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(time);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().extract()
                .as(ReservationTimeResponse.class)
                .id();
    }

    protected void checkHttpStatusOk(
            StandardSoftAssertionsProvider softAssertionsProvider, ExtractableResponse<Response> response) {
        softAssertionsProvider.assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
    }

    protected void checkHttpStatusCreated(
            StandardSoftAssertionsProvider softAssertionsProvider, ExtractableResponse<Response> response) {
        softAssertionsProvider.assertThat(response.statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
    }

    protected void checkHttpStatusNoContent(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    protected void checkHttpStatusBadRequest(
            StandardSoftAssertionsProvider softAssertionsProvider, ExtractableResponse<Response> response) {
        softAssertionsProvider.assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    protected void checkHttpStatusNotFound(
            StandardSoftAssertionsProvider softAssertionsProvider, ExtractableResponse<Response> response) {
        softAssertionsProvider.assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
