package roomescape.acceptance;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.context.jdbc.Sql;
import roomescape.BasicAcceptanceTest;

@Sql("/init-for-login.sql")
class LoginAcceptanceTest extends BasicAcceptanceTest {
    @TestFactory
    @DisplayName("role이 USER인 Member는 Admin 페이지에 접속하지 못 한다")
    Stream<DynamicTest> moveNotAdminPageTest() {
        AtomicReference<String> userToken = new AtomicReference<>();
        return Stream.of(
                dynamicTest("role이 USER인 계정으로 로그인을 한다", () -> {
                    userToken.set(LoginUtil.login("email1", "qq1"));
                }),
                dynamicTest("admin 페이지에 접속한다", () -> moveToAdminPage(userToken.get(), 401)),
                dynamicTest("admin 예약 관리 페이지에 접속한다", () -> moveToReservationAdminPage(userToken.get(), 401)),
                dynamicTest("admin 시간 관리 페이지에 접속한다", () -> moveToTimeAdminPage(userToken.get(), 401)),
                dynamicTest("admin 테마 관리 페이지에 접속한다", () -> moveToThemeAdminPage(userToken.get(), 401))
        );
    }

    @TestFactory
    @DisplayName("role이 ADMIN인 Member는 Admin 페이지에 접속가능하다")
    Stream<DynamicTest> moveAdminPageTest() {
        AtomicReference<String> adminToken = new AtomicReference<>();
        return Stream.of(
                dynamicTest("role이 USER인 계정으로 로그인을 한다", () -> {
                    adminToken.set(LoginUtil.login("admin", "admin"));
                }),
                dynamicTest("admin 페이지에 접속한다", () -> moveToAdminPage(adminToken.get(), 200)),
                dynamicTest("admin 예약 관리 페이지에 접속한다", () -> moveToReservationAdminPage(adminToken.get(), 200)),
                dynamicTest("admin 시간 관리 페이지에 접속한다", () -> moveToTimeAdminPage(adminToken.get(), 200)),
                dynamicTest("admin 테마 관리 페이지에 접속한다", () -> moveToThemeAdminPage(adminToken.get(), 200))
        );
    }

    private void moveToAdminPage(String token, int expectedHttpCode) {
        RestAssured.given().log().all()
                .cookies("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(expectedHttpCode);
    }

    private void moveToReservationAdminPage(String token, int expectedHttpCode) {
        RestAssured.given().log().all()
                .cookies("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(expectedHttpCode);
    }

    private void moveToThemeAdminPage(String token, int expectedHttpCode) {
        RestAssured.given().log().all()
                .cookies("token", token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(expectedHttpCode);
    }

    void moveToTimeAdminPage(String token, int expectedHttpCode) {
        RestAssured.given().log().all()
                .cookies("token", token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(expectedHttpCode);
    }
}
