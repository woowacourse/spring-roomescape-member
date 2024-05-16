package roomescape.admin.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.admin.dto.SaveAdminReservationRequest;
import roomescape.auth.dto.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerIntegrationTest {

  @LocalServerPort
  int randomServerPort;

  @BeforeEach
  public void setUp() {
    RestAssured.port = randomServerPort;
  }

  @DisplayName("관리자 권한으로 예약을 진행한다.")
  @Test
  void saveAdminReservation() {
    // given
    final SaveAdminReservationRequest saveAdminReservationRequest = new SaveAdminReservationRequest(
        LocalDate.now().plusDays(1), 1L, 1L, 1L);
    final LoginRequest loginRequest = new LoginRequest("neo@example.com", "password123");
    final Response response = RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(loginRequest)
        .post("/login");
    final Cookies cookies = response.getDetailedCookies();
    final String token = cookies.getValue("token");

    //then
    RestAssured.given().log().all()
        .cookie("token", token)
        .contentType(ContentType.JSON)
        .body(saveAdminReservationRequest)
        .when().post("admin/reservations")
        .then().log().all()
        .statusCode(201)
        .body("id", is(17));
  }
}
