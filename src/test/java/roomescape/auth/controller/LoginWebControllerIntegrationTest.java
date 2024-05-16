package roomescape.auth.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginWebControllerIntegrationTest {

  @LocalServerPort
  int randomServerPort;

  @BeforeEach
  public void setup() {
    RestAssured.port = randomServerPort;
  }

  @DisplayName("/login으로 요청하면 200응답이 넘어온다.")
  @Test
  void requestLoginPageTest() {
    RestAssured.given().log().all()
        .when().get("/login")
        .then().log().all()
        .statusCode(200);
  }
}
