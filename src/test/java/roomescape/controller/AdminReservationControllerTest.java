package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.LoginRequest;
import roomescape.dto.ReservationRequest;
import roomescape.dto.TokenResponse;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/testdata.sql")
class AdminReservationControllerTest {

    @Autowired
    LoginController loginController;

    @Test
    @DisplayName("Admin 권한의 사용자가 예약을 추가할 수 있다.")
    void create() {
        final String accessToken = getAccessToken();
        final ReservationRequest reservationRequest = new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L, 1L);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    private String getAccessToken() {
        final ResponseEntity<TokenResponse> response = loginController.login(new LoginRequest("789@email.com", "789"), new MockHttpServletResponse());
        return response.getBody().getAccessToken();
    }
}
