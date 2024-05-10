package roomescape.auth.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.dto.LoginRequest;
import roomescape.globar.infra.JwtTokenProvider;


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
    LoginRequest request = new LoginRequest("user@mail.com", "1234");
    String token = jwtTokenProvider.createToken(request.email(), "어드민");

    // Then
    RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(request)
        .post("/login")
        .then().log().all()
        .statusCode(200)
        .cookie("token", notNullValue());
  }

  @DisplayName("로그인 상태를 반환한다.")
  @Test
  void checkLogin() {
    // Given
    String name = "켈리";
    LoginRequest request = new LoginRequest("kelly@example.com", name);
    String token = jwtTokenProvider.createToken(request.email(), name);

    // Then
    RestAssured.given().log().all()
        .cookie("token", token)
        .get("/login/check")
        .then().log().all()
        .statusCode(200)
        .body("name", is(name));
  }
}
