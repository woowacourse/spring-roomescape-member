package roomescape.admin.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.admin.dto.SaveAdminReservationRequest;
import roomescape.auth.dto.LoginRequest;

class AdminControllerIntegrationTest {

  @DisplayName("관리자 권한으로 예약을 진행한다.")
  @Test
  void saveAdminReservation() {
    // given
    SaveAdminReservationRequest saveAdminReservationRequest = new SaveAdminReservationRequest(
        LocalDate.now().plusDays(1), 1L, 1L, 1L);

    String name = "켈리";
    LoginRequest loginRequest = new LoginRequest("kelly@example.com", "password123");
    Response response = RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(loginRequest)
        .post("/login");
    Cookies cookies = response.getDetailedCookies();
    String token = cookies.getValue("token");

    RestAssured.given().log().all()
        .cookie("token", token)
        .contentType(ContentType.JSON)
        .body(saveAdminReservationRequest)
        .when().post("admin/reservations")
        .then().log().all()
        .statusCode(201)
        .body("id", is(17));
    // when

    //then

  }

}
