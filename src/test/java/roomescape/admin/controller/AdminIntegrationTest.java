package roomescape.admin.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import roomescape.admin.dto.AdminReservationRequest;
import roomescape.auth.domain.Token;
import roomescape.auth.provider.CookieProvider;
import roomescape.auth.provider.model.TokenProvider;
import roomescape.model.IntegrationTest;

public class AdminIntegrationTest extends IntegrationTest {

    private static final long ADMIN_ROLE_ID = 1;
    private static final long MEMBER_ROLE_ID = 2;

    @Autowired
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("정상적인 요청에 대하여 관리자는 예약할 수 있다.")
    void adminCanReservation() {
        ResponseCookie cookie = makeMemberRoleCookie(ADMIN_ROLE_ID);

        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(TODAY.plusDays(1), 1L, 1L,
                ADMIN_ROLE_ID);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.toString())
                .body(adminReservationRequest)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("접근 가능한 권한이 아닌 경우 예외를 던진다.")
    void memberCanNotReservationAdmin() {
        ResponseCookie cookie = makeMemberRoleCookie(MEMBER_ROLE_ID);

        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(TODAY.plusDays(1), 1L, 1L,
                MEMBER_ROLE_ID);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.toString())
                .body(adminReservationRequest)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(403);
    }

    private ResponseCookie makeMemberRoleCookie(long id) {
        Token token = tokenProvider.getAccessToken(id);
        return CookieProvider.setCookieFrom(token);
    }
}
