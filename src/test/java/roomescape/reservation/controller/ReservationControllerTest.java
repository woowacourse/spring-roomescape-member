package roomescape.reservation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.MemberFixture;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationRequest;

import java.time.LocalDate;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @Test
    @DisplayName("예약 목록을 조회한다.")
    void getReservations() {
        Map<String, String> cookies = MemberFixture.loginUser();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자가 예약을 추가한다.")
    void postReservationInAdminPage() {
        LocalDate fixedDate = LocalDate.of(2026, 5, 15);
        long expectedTimeId = 1L;
        long expectedThemeId = 1L;
        long expectedMemberId = 1L;

        Map<String, String> cookies = MemberFixture.loginAdmin();
        AdminReservationRequest request = new AdminReservationRequest(fixedDate, expectedTimeId, expectedThemeId, expectedMemberId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

    }

    @Test
    @DisplayName("존재하는 ID로 삭제 요청 시 성공적으로 처리되어야 한다.")
    void deleteExistingReservation() {
        long existingId = 1L;
        Map<String, String> cookies = MemberFixture.loginAdmin();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when().delete("/reservations/" + existingId)
                .then().log().all()
                .statusCode(204);
    }


    @Test
    @DisplayName("존재하지 않는 ID로 삭제 요청 시 404 응답이 반환되어야 한다.")
    void deleteNonExistingReservation() {
        long nonExistingId = 999L;

        Map<String, String> cookies = MemberFixture.loginUser();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when().delete("/reservations/" + nonExistingId)
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("사용자가 예약을 추가한다.")
    void postReservationInUserPage() {
        LocalDate fixedDate = LocalDate.of(2026, 5, 15);
        long expectedTimeId = 1L;
        long expectedThemeId = 1L;

        Map<String, String> cookies = MemberFixture.loginUser();

        ReservationRequest request = new ReservationRequest(fixedDate, expectedTimeId, expectedThemeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("사용자가 예약 가능한 시간을 조회한다.")
    void getAvailableReservationTimes() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        long themeId = 1L;

        Map<String, String> cookies = MemberFixture.loginAdmin();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when().get("/reservations/times?date=" + date + "&themeId=" + (int) themeId)
                .then().log().all()
                .statusCode(200);
    }
}
