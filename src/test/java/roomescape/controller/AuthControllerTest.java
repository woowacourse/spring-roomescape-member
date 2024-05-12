package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import java.util.Map;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/truncate.sql", "/memberData.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("DB에 존재하는 member 정보로 로그인 시도시 cookie에 jwt 토큰을 담아 반환한다.")
    void login() {
        Map<String, String> validMemberParams = Map.of(
                "email", "ddang@google.com",
                "password", "password"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(validMemberParams)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("token");
    }

    @Test
    @DisplayName("DB에 존재하지 않는 member 정보로 로그인 시도시 404를 반환한다.")
    void loginFail() {
        Map<String, String> invalidMemberParams = Map.of(
                "email", "invalidMember@google.com",
                "password", "invalidPassword"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(invalidMemberParams)
                .when().post("/login")
                .then().log().all()
                .statusCode(404);
    }
}
