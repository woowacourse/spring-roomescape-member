package roomescape.controller.login;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Sql(value = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {

    @Autowired
    LoginController loginController;
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("존재하는 사용자 이메일과 비밀번호로 로그인을 하면 토큰을 응답한다.")
    void postLogin200PresentCredential() {
        final TokenRequest request = new TokenRequest("seyang@test.com", "seyang");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .header("Keep-Alive", is("timeout=60"))
                .header("Set-Cookie", containsString("Path=/"))
                .header("Set-Cookie", containsString("HttpOnly"))
                .cookie("token", notNullValue());
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 이메일로 로그인을 하면 401 을 응답한다.")
    void postLogin401NotExistEmail() {
        final TokenRequest request = new TokenRequest("no@test.com", "no");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(401)
                .body("message", containsString("일치"));
    }

    @Test
    @DisplayName("토큰으로 정보를 요청하면 200 과 권한을 응답한다.")
    void getInfo200Authorization() {
        final TokenRequest request = new TokenRequest("seyang@test.com", "seyang");

        final ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();
        final Cookie token = extract.detailedCookie("token");

        RestAssured.given().log().all()
                .cookie(token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", is("새양"));
    }
}
