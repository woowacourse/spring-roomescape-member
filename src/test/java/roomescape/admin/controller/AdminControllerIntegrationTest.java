package roomescape.admin.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import roomescape.admin.dto.AdminReservationRequest;
import roomescape.auth.domain.Token;
import roomescape.auth.provider.CookieProvider;
import roomescape.auth.provider.model.TokenProvider;
import roomescape.model.IntegrationTest;

public class AdminControllerIntegrationTest extends IntegrationTest {

    private Token token;
    private ResponseCookie cookie;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        this.token = tokenProvider.getAccessToken(1);
        this.cookie = CookieProvider.setCookieFrom(token);
    }

    @Test
    @DisplayName("정상적인 요청에 대하여 관리자는 예약할 수 있다.")
    void adminCanReservation() {
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(TODAY.plusDays(1), 1L, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.toString())
                .body(adminReservationRequest)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }
}
