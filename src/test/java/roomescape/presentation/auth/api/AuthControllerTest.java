package roomescape.presentation.auth.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.persistence.fakerepository.FakeMemberRepository;
import roomescape.presentation.AbstractControllerTest;
import roomescape.presentation.auth.dto.LoginRequestDto;
import roomescape.presentation.auth.dto.MemberRequestDto;

class AuthControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FakeMemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        memberRepository.clear();
    }

    @DisplayName("회원 가입을 한다.")
    @Test
    void signup() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, name);

        // when
        Long memberId = Long.valueOf(
                RestAssured
                        .given()
                        .log().all()
                        .contentType("application/json")
                        .body(memberRequestDto)
                        .when()
                        .post("/members")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .header("Location")
                        .replace("/members/", "")
        );

        // then
        assertAll(
                () -> assertThat(memberRepository.findById(memberId))
                        .isPresent(),
                () -> assertThat(memberRepository.findById(memberId).get().getName())
                        .isEqualTo(name),
                () -> assertThat(memberRepository.findById(memberId).get().getEmail())
                        .isEqualTo(email)
        );
    }

    @DisplayName("회원 가입 시 중복된 이메일로 가입을 시도하면 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenDuplicateEmail() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, name);
        RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(memberRequestDto)
                .when()
                .post("/members")
                .then()
                .log().all()
                .statusCode(201);

        // when
        // then
        RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(memberRequestDto)
                .when()
                .post("/members")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("이미 존재하는 이메일입니다."));
    }

    @DisplayName("로그인 시 존재하지 않는 이메일로 로그인 시도하면 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenInvalidEmailOrPassword() {
        // given
        String email = "test@email.com";
        String password = "password";
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);

        // when
        // then
        RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(loginRequestDto)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("이메일 또는 비밀번호가 잘못되었습니다."));
    }

    @DisplayName("로그인 시 일치하지 않는 비밀번호로 로그인 시도하면 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenInvalidPassword() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, name);
        RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(memberRequestDto)
                .when()
                .post("/members")
                .then()
                .log().all()
                .statusCode(201);
        String invalidPassword = "invalidPassword";
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, invalidPassword);

        // when
        // then
        RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(loginRequestDto)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("이메일 또는 비밀번호가 잘못되었습니다."));
    }

    @DisplayName("성공적인 로그인 시 쿠키에 AccessToken이 저장된다.")
    @Test
    void login() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, name);
        RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(memberRequestDto)
                .when()
                .post("/members")
                .then()
                .log().all()
                .statusCode(201);
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);

        // when
        String accessToken = RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(loginRequestDto)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .cookie("token");

        // then
        assertThat(accessToken).isNotNull();
    }

    @DisplayName("로그인 체크 시 로그인된 사용자의 정보를 반환한다.")
    @Test
    void checkLogin() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, name);
        RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(memberRequestDto)
                .when()
                .post("/members")
                .then()
                .log().all()
                .statusCode(201);
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
        String accessToken = RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(loginRequestDto)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .cookie("token");

        // when
        String loginCheckResponse = RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .cookie("token", accessToken)
                .when()
                .get("/login/check")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("name");

        // then
        assertThat(loginCheckResponse)
                .isEqualTo("test");
    }
}
