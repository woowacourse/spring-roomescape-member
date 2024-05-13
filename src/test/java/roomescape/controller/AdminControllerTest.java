package roomescape.controller;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/truncate.sql", "/memberData.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class AdminControllerTest {

    @LocalServerPort
    private int port;

    private final Map<String, String> memberParams = Map.of(
            "email", "ddang@google.com",
            "password", "password"
    );

    private final Map<String, String> adminParams = Map.of(
            "email", "admin@google.com",
            "password", "password"
    );

    @Test
    @DisplayName("/ 로 get 요청을 보내면 응답 코드는 200이다.")
    void getMainPage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자로 로그인한 경우, /admin 으로 get 요청을 보내면 응답 코드는 200이다.")
    void getAdminPage() {
        String token = loginWith(adminParams);

        RestAssured.given().log().all()
                .cookie("token", token)
                .port(port)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("일반 사용자로 로그인한 경우, /admin 으로 get 요청을 보내면 응답 코드는 403이다.")
    void memberGetAdminPage() {
        String token = loginWith(memberParams);

        RestAssured.given().log().all()
                .cookie("token", token)
                .port(port)
                .when().get("/admin")
                .then().log().all()
                .statusCode(403);
    }

    @Test
    @DisplayName("관리자로 로그인한 경우, /admin/reservation 으로 get 요청을 보내면 응답 코드는 200이다.")
    void getAdminReservationPage() {
        String token = loginWith(adminParams);

        RestAssured.given().log().all()
                .cookie("token", token)
                .port(port)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("일반 사용자로 로그인한 경우, /admin/reservation 으로 get 요청을 보내면 응답 코드는 403이다.")
    void memberGetAdminReservationPage() {
        String token = loginWith(memberParams);

        RestAssured.given().log().all()
                .cookie("token", token)
                .port(port)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(403);
    }

    private String loginWith(Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().cookie("token");
    }
}
