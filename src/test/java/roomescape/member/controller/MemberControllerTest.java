package roomescape.member.controller;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.LoginRequest;
import roomescape.member.service.MemberService;
import roomescape.reservation.dao.FakeMemberDao;
import roomescape.util.ControllerTest;

@DisplayName("사용자 API 테스트")
class MemberControllerTest extends ControllerTest {
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
        // TODO 회원 가입 추가하고 살릴것
        /*RestAssured.given().log().all()
                .body(new LoginRequest("admin@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .header("Set-Cookie", containsString("token"));*/
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
}
