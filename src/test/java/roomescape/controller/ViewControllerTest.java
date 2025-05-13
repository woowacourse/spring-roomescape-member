package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ViewControllerTest {

    @BeforeEach
    void setUp() {
        Map<String, Object> adminParams = new HashMap<>();
        adminParams.put("name", "어드민");
        adminParams.put("email", "admin@gmail.com");
        adminParams.put("password", "1234");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(adminParams)
                .post("/signup/admin");

        Map<String, Object> normalParams = new HashMap<>();
        normalParams.put("name", "일반");
        normalParams.put("email", "user@gmail.com");
        normalParams.put("password", "1234");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(normalParams)
                .post("/signup");
    }

    @Test
    @DisplayName("/ GET 요청에 응답한다")
    void welcome_page() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin GET 요청에 응답한다")
    void admin_page() {
        String token = getAdminToken();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin/reservation GET 요청에 응답한다")
    void admin_adminReservation_page() {
        String token = getAdminToken();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin api에 접근 시, 관리자가 아닌 경우 403을 응답한다")
    void admin_not_admin() {
        Map<String, Object> memberParams = new HashMap<>();
        memberParams.put("email", "user@gmail.com");
        memberParams.put("password", "1234");

        String accessToken = RestAssured
                .given().log().all()
                .body(memberParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(403);
    }

    private String getAdminToken() {
        Map<String, Object> memberParams = new HashMap<>();
        memberParams.put("email", "admin@gmail.com");
        memberParams.put("password", "1234");

        String accessToken = RestAssured
                .given().log().all()
                .body(memberParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        return accessToken;
    }
}
