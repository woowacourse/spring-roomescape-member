package roomescape.admin.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.dto.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminWebControllerIntegrationTest {

  @LocalServerPort
  int randomServerPort;

  @BeforeEach
  public void setup() {
    RestAssured.port = randomServerPort;
  }

  @DisplayName("/admin으로 요청하면 200응답이 넘어온다.")
  @Test
  void requestAdminPageTest() {
    final LoginRequest request = new LoginRequest("neo@example.com", "password123");
    final Response response = RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(request)
        .post("/login");
    final Cookies cookies = response.getDetailedCookies();
    final String token = cookies.getValue("token");

    // Then
    RestAssured.given().log().all()
        .cookie("token", token)
        .when().get("/admin")
        .then().log().all()
        .statusCode(200);
  }

  @DisplayName("관리자가 아닌 유저가 /admin으로 요청하면 400응답이 넘어온다.")
  @Test
  void requestAdminPageByNotAdminMemberTest() {
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
        .when().get("/admin")
        .then().log().all()
        .statusCode(400);
  }

  @DisplayName("/admin/reservation으로 요청하면 200응답이 넘어온다.")
  @Test
  void requestReservationPageTest() {
    // given
    final LoginRequest request = new LoginRequest("neo@example.com", "password123");
    final Response response = RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(request)
        .post("/login");
    final Cookies cookies = response.getDetailedCookies();
    final String token = cookies.getValue("token");

    // then
    RestAssured.given().log().all()
        .cookie("token", token)
        .when().get("/admin/reservation")
        .then().log().all()
        .statusCode(200);
  }

  @DisplayName("관리자가 아닌 유저가 /admin/reservation으로 요청하면 400응답이 넘어온다.")
  @Test
  void requestReservationPageByNotAdminMemberTest() {
    // given
    final LoginRequest request = new LoginRequest("kelly@example.com", "password123");
    final Response response = RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(request)
        .post("/login");
    final Cookies cookies = response.getDetailedCookies();
    final String token = cookies.getValue("token");

    // then
    RestAssured.given().log().all()
        .cookie("token", token)
        .when().get("/admin/reservation")
        .then().log().all()
        .statusCode(400);
  }
}
