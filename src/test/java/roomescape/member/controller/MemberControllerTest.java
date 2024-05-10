package roomescape.member.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.dto.LoginCheckResponse;
import roomescape.member.dto.LoginRequest;
import roomescape.util.ControllerTest;

@DisplayName("사용자 API 테스트")
class MemberControllerTest extends ControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setData() {
        String sql = """ 
                  INSERT INTO member(name, email, password, role)
                  VALUES ('관리자', 'admin@email.com', 'password', 'ADMIN');
                """;
        jdbcTemplate.update(sql);
    }

    @DisplayName("로그인 폼 페이지 조회에 성공한다.")
    @Test
    void popularPage() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200)
                .body(containsString("Login"));
    }

    @DisplayName("로그인 시 가능한 유저일 경우 200을 반환한다.")
    @Test
    void login() {
        RestAssured.given().log().all()
                .body(new LoginRequest("admin@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .header("Set-Cookie", containsString("token"));
    }

    @DisplayName("로그인 시, 아이디 혹은 비밀번호가 일치하지 않는 유저일 경우 400을 반환한다.")
    @Test
    void loginNotMatch() {
        RestAssured.given().log().all()
                .body(new LoginRequest("admin@email.com", "pp"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("로그인 시, 빈 email에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "           "})
    void loginBlankEmail(String email) {
        RestAssured.given().log().all()
                .body(new LoginRequest(email, "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("로그인 시, 빈 비밀번호에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "           "})
    void longBlankPassword(String password) {
        RestAssured.given().log().all()
                .body(new LoginRequest("admin@email.com", password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("인증 정보를 조회할 수 있다.")
    @Test
    void loginCheck() {
        String accessToken = RestAssured
                .given().log().all()
                .body(new LoginRequest("admin@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract()
                .cookie("token");

        LoginCheckResponse loginCheckResponse = RestAssured
                .given().log().all()
                .cookie("token", accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().as(LoginCheckResponse.class);
    }

    @DisplayName("멤버의 전체 정보를 조회할 수 있다.")
    @Test
    void readMembers() {
        RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200);
    }
}
