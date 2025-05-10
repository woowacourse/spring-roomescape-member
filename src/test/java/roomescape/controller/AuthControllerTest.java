package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.presentation.dto.response.MemberNameResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {

    @BeforeEach
    void setUp() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "일반");
        params.put("email", "user@gmail.com");
        params.put("password", "1234");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .post("/signup");
    }

    @Test
    @DisplayName("/signup POST 요청을 통해 회원가입 할 수 있다")
    void signup() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "회원");
        params.put("email", "admin@gmail.com");
        params.put("password", "1234");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/signup")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/signup/admin POST 요청을 통해 어드민 권한의 회원가입 할 수 있다")
    void signup_admin() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "회원");
        params.put("email", "admin@gmail.com");
        params.put("password", "1234");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/signup/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/login POST 요청을 통해 로그인에 성공하면 토큰을 발급받는다")
    void login_token() {
        Map<String, Object> params = new HashMap<>();
        params.put("email", "user@gmail.com");
        params.put("password", "1234");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .header("Set-Cookie", notNullValue());
    }

    @Test
    @DisplayName("/login POST 요청시 존재하지 않는 회원일 시 401을 응답한다")
    void login_member_not_exist() {
        Map<String, Object> params = new HashMap<>();
        params.put("email", "may@gmail.com");
        params.put("password", "1234");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("/login/check GET 요청을 통해 토큰 기반 사용자 이름을 확인할 수 있다")
    void login_check_name() {

        Map<String, Object> params = new HashMap<>();
        params.put("email", "user@gmail.com");
        params.put("password", "1234");

        String accessToken = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        MemberNameResponse nameResponse = RestAssured
                .given().log().all()
                .cookie("token", accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().as(MemberNameResponse.class);

        assertThat(nameResponse.name()).isEqualTo("일반");
    }

    @Test
    @DisplayName("/login/check GET 요청시 올바르지 않은 토큰일 시 401을 응답한다")
    void login_check_not_valid_token() {
        RestAssured
                .given().log().all()
                .cookie("token", "abc.def.g")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }
}
