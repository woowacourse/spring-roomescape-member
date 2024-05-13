package roomescape.acceptance;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PageAcceptanceTest extends AcceptanceTest {

    @ParameterizedTest
    @ValueSource(strings = {"/admin", "/admin/reservation", "/admin/time", "/admin/theme"})
    @DisplayName("관리자가 관리자 페이지에 접근하면 200을 응답한다.")
    void respondOkWhenAdminAccessAdminPage(final String adminPath) {
        final String accessToken = getAccessToken("nyangin@email.com");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get(adminPath)
                .then().log().all()
                .statusCode(200)
                .body(containsString("<!DOCTYPE html>"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/admin", "/admin/reservation", "/admin/time", "/admin/theme"})
    @DisplayName("사용자가 관리자 페이지에 접근하면 403을 응답한다.")
    void respondForbiddenWhenMemberAccessAdminPage(final String adminPath) {
        final String accessToken = getAccessToken("mia@email.com");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get(adminPath)
                .then().log().all()
                .statusCode(403);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/", "/reservation", "/login"})
    @DisplayName("사용자가 사용자 페이지에 접근하면 200을 응답한다.")
    void respondOkWhenMemberAccessMemberPage(String path) {
        RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(200)
                .body(containsString("<!DOCTYPE html>"));
    }
}
