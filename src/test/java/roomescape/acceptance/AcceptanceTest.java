package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.reservation.MemberReservationSaveRequest;
import roomescape.dto.reservation.ReservationTimeSaveRequest;
import roomescape.dto.theme.ThemeSaveRequest;
import roomescape.dto.auth.TokenRequest;
import roomescape.dto.auth.TokenResponse;

import static roomescape.TestFixture.*;

@Sql("/test-schema.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected Long saveReservationTime() {
        final ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(START_AT_SIX);

        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .jsonPath().get("id");

        return Long.valueOf(id);
    }

    protected Long saveTheme() {
        final ThemeSaveRequest request
                = new ThemeSaveRequest(THEME_HORROR_NAME, THEME_HORROR_DESCRIPTION, THEME_HORROR_THUMBNAIL);

        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract()
                .jsonPath().get("id");

        return Long.valueOf(id);
    }

    protected Long saveReservation(final Long timeId, final Long themeId) {
        final String accessToken = getAccessToken(MEMBER_MIA_EMAIL);
        final MemberReservationSaveRequest request = new MemberReservationSaveRequest(DATE_MAY_EIGHTH, timeId, themeId);

        Integer id = RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract()
                .jsonPath().get("id");

        return Long.valueOf(id);
    }

    protected String getAccessToken(final String email) {
        return RestAssured.given().log().all()
                .body(new TokenRequest(email, MEMBER_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .extract().as(TokenResponse.class).accessToken();
    }
}
