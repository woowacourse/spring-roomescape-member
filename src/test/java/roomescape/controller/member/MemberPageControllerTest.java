package roomescape.controller.member;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.member.SignupRequest;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql"})
class MemberPageControllerTest {

    @Autowired
    private MemberController memberController;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("index 페이지를 요청한다.")
    @Test
    void index() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("로그인 상태가 아니라면 /reservation 요청시 /login으로 이동한다.")
    @Test
    void reservationWithoutLogin() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .contentType(MediaType.TEXT_HTML_VALUE)
                .body(containsString("<title>Login</title>"));
    }

    @DisplayName("로그인 상태에서 /reservation 요청시 정상 접속한다.")
    @Test
    void reservationWithLogin() {
        memberController.createMember(new SignupRequest("email@email.com", "password", "name"));
        Map<String, Object> params = Map.of(
                "email", "email@email.com",
                "password", "password"
        );
        String accessToken = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");

        // 로그인 상태여서 /reservation 에 접속하면 정상 응답한다.
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200)
                .contentType(MediaType.TEXT_HTML_VALUE)
                .body(containsString("<title>방탈출 예약 페이지</title>"));
    }
}
