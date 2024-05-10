package roomescape.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import roomescape.auth.dto.LoginRequestDto;
import roomescape.member.dto.MemberRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql"})
public class AuthAcceptanceTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("등록된 사용자에게 토큰을 발급할 수 있다.")
    @Test
    void tokenLogin() {
        RestAssured.given()
                .log().all()
                .body(new MemberRequestDto("hotea@hotea.com", "1234", "hotea"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().post("/signup")
                .then().statusCode(201);

        RestAssured.given()
                .log().all()
                .body(new LoginRequestDto("1234", "hotea@hotea.com"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().post("/login")
                .then().statusCode(200).cookie("token");
    }

    @DisplayName("토큰을 통해 사용자 정보를 확인할 수 있다..")
    @Test
    void tokenValidate() {
        RestAssured.given()
                .log().all()
                .body(new MemberRequestDto("hotea@hotea.com", "1234", "hotea"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().post("/signup")
                .then().statusCode(201);

        String token = RestAssured.given()
                .log().all()
                .body(new LoginRequestDto("1234", "hotea@hotea.com"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().post("/login")
                .then().statusCode(200).extract().cookie("token");

        RestAssured.given()
                .log().all()
                .header("Cookie", "token=" + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().get("/login/check")
                .then().statusCode(200);
    }
}
