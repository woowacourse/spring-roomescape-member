package roomescape.controller.view;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.AuthenticationProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminViewControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자는 /admin이 붙은 페이지로 정상적으로 이동할 수 있다.")
    @Sql(scripts = {"/truncate-data.sql", "/member-data.sql"})
    void moveToAdminPage_Success() {
        String token = AuthenticationProvider.loginAdmin();
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("일반 사용자는 /admin이 붙은 페이지로 이동할 수 없다.")
    @Sql(scripts = {"/truncate-data.sql", "/member-data.sql"})
    void moveToAdminPageWithMember_Success() {
        String token = AuthenticationProvider.loginMember();
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(403);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(403);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(403);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(403);
    }
}
