package roomescape.auth.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.auth.service.dto.SignUpRequest;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void init(){
        RestAssured.port = port;
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    void login() {
        //given
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest("lini","lini@email.com","lini123"))
                .when().post("/signup")
                .then().log().all();

        //when&then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("lini123","lini@email.com"))
                .when().post("/login")
                .then().log().all()
                .assertThat().statusCode(200);
    }

    @DisplayName("회원가입 성공 테스트")
    @Test
    void signUp() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest("lini","lini@email.com","lini123"))
                .when().post("/signup")
                .then().log().all()
                .assertThat().statusCode(201).header("location","/login");
    }

    @DisplayName("인증 조회 성공 테스트")
    @Test
    void check() {
        //given
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest("lini","lini@email.com","lini123"))
                .when().post("/signup")
                .then().log().all();

        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("lini123", "lini@email.com"))
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie");

        //when&then
        RestAssured.given().log().all()
                .cookie(token)
                .when().get("/login/check")
                .then().log().all()
                .assertThat().statusCode(200).body("name",is("lini"));
    }
}
