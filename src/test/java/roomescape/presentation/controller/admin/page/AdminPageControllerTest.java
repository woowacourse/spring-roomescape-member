package roomescape.presentation.controller.admin.page;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminPageControllerTest {

    private String adminToken;
    private String userToken;

    @BeforeEach
    void createToken() {
        Map<String, String> adminParameters = Map.of(
                "email", "sooyang@woowa.com",
                "password", "1234"
        );
        Map<String, String> userParameters = Map.of(
                "email", "user@woowa.com",
                "password", "user1234"
        );
        adminToken = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(adminParameters)
                .when().post("/login")
                .then().log().all()
                .extract().response().getCookie("token");
        userToken = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userParameters)
                .when().post("/login")
                .then().log().all()
                .extract().response().getCookie("token");
    }

    @Test
    @DisplayName("/admin 관리자 인덱스 페이지 관리자 요청을 응답한다.")
    void test1() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin 관리자 인덱스 페이지 일반 사용자 요청을 거부한다.")
    void test2() {
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("/admin/reservation 페이지 관리자 요청을 응답한다.")
    void test3() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin/reservation 페이지 일반 사용자 요청을 거부한다.")
    void test4() {
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("/admin/time 페이지 관리자 요청을 응답한다.")
    void test5() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin/time 페이지 관리자 요청을 응답한다.")
    void test6() {
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("/admin/theme 페이지 관리자 요청을 응답한다.")
    void test7() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin/theme 페이지 일반 사용자 요청을 응답한다.")
    void test8() {
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(401);
    }
}