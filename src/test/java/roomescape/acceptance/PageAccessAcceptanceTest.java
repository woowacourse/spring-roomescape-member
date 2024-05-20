package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import roomescape.controller.exception.CustomExceptionResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("관리자만 접근 가능한 페이지가 있다.")
class PageAccessAcceptanceTest extends BaseAcceptanceTest {

    private static final String ADMIN_MAIN = "/admin";
    private static final String ADMIN_RESERVATION = "/admin/reservation";
    private static final String ADMIN_TIME = "/admin/time";
    private static final String ADMIN_THEME = "/admin/theme";

    @DisplayName("관리자는 접근 가능하다.")
    @ParameterizedTest
    @ValueSource(strings = {ADMIN_MAIN, ADMIN_RESERVATION, ADMIN_TIME, ADMIN_THEME})
    void adminPageAccess_success(String path) {
        sendGetRequestWithToken(path, tokenFixture.adminToken)
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("고객은 접근 할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {ADMIN_MAIN, ADMIN_RESERVATION, ADMIN_TIME, ADMIN_THEME})
    void adminPageAccess_customer_fail(String path) {
        CustomExceptionResponse response = sendGetRequestWithToken(path, tokenFixture.customerToken)
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract().as(CustomExceptionResponse.class);

        assertAll(
                () -> assertThat(response.title()).contains("접근 권한이 없습니다."),
                () -> assertThat(response.detail()).contains("")
        );
    }

    @DisplayName("로그인 안한 사용자는 접근 할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {ADMIN_MAIN, ADMIN_RESERVATION, ADMIN_TIME, ADMIN_THEME})
    void adminPageAccess_guest_fail(String path) {
        CustomExceptionResponse response = sendGetRequestWithoutToken(path)
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract().as(CustomExceptionResponse.class);

        assertAll(
                () -> assertThat(response.title()).contains("토큰이 검증이 실패했습니다."),
                () -> assertThat(response.detail()).contains("토큰이 존재하지 않습니다.")
        );
    }


    private ValidatableResponse sendGetRequestWithToken(String path, String token) {
        return RestAssured.given().log().all()
                .cookie("token", token)
                .when().get(path)
                .then().log().all();
    }

    private ValidatableResponse sendGetRequestWithoutToken(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all();
    }
}
