package roomescape.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.dto.LoginRequest;
import roomescape.global.infra.JwtTokenProvider;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerIntegrationTest {

  @LocalServerPort
  int randomServerPort;
  @Autowired
  JwtTokenProvider jwtTokenProvider;

  @BeforeEach
  public void setUp() {
    RestAssured.port = randomServerPort;
  }

  @DisplayName("로그인 시도시 토큰을 발급하여 쿠키에 저장한다.")
  @Test
  void login() {
    // Given
    final LoginRequest request = new LoginRequest("kelly@example.com", "password123");
    // When
    final Response response = RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(request)
        .post("/login");
    final Cookies cookies = response.getDetailedCookies();
    final String token = cookies.getValue("token");
    // Then
    assertThat(jwtTokenProvider.validateToken(token)).isTrue();
  }

  @DisplayName("가입하지 않은 이메일로 로그인을 시도할 시 예외를 발생한다.")
  @Test
  void loginWithInvalidEmail() {
    // Given
    final LoginRequest request = new LoginRequest("user@mail.com", "1234");

    // Then
    RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(request)
        .post("/login")
        .then().log().all()
        .statusCode(400);
  }

  @DisplayName("올바르지 않은 패스워드로 로그인을 시도할 시 예외를 발생한다.")
  @Test
  void loginWithInvalidPassword() {
    // Given
    final LoginRequest request = new LoginRequest("kelly@example.com", "1234");

    // Then
    RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(request)
        .post("/login")
        .then().log().all()
        .statusCode(400);
  }

  @DisplayName("로그인 상태를 반환한다.")
  @Test
  void checkLogin() {
    // Given
    final String name = "켈리";
    final LoginRequest request = new LoginRequest("kelly@example.com", "password123");
    final Response response = RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(request)
        .post("/login");
    final Cookies cookies = response.getDetailedCookies();
    final String token = cookies.getValue("token");

    // Then
    RestAssured.given().log().all()
        .cookie("token", token)
        .get("/login/check")
        .then().log().all()
        .statusCode(200)
        .body("name", is(name));
  }
}
