package roomescape.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.service.request.MemberLoginRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/member_test_data.sql")
public class AuthIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("로그인 정보가 데이터베이스 정보외 일치하면 로그인에 성공한다.")
    void loginSuccess() {
        MemberLoginRequest request = new MemberLoginRequest("user1@gmail.com", "user1");

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(request)
                .response()
                .when().post("/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그인 정보가 데이터베이스 정보외 일치하지 않으면 로그인에 실패한다.")
    void loginFailure() {
        MemberLoginRequest request = new MemberLoginRequest("user1@gmail.com", "1234");

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/login")
                .then().log().all()
                .statusCode(401);
    }
}
