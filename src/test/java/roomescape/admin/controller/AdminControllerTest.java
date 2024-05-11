package roomescape.admin.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.admin.dto.AdminReservationRequest;
import roomescape.base.BaseTest;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminControllerTest extends BaseTest {
    private static final String DATE = "3000-01-01";

    @Test
    @DisplayName("관리자가 {date, themeId, timeId, memberId}로 예약을 생성할 수 있다.")
    void saveReservationTest() {
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(
                LocalDate.of(3000, 1, 1), 1L, 1L, 1L);

        RestAssured.given()
                .cookie("token", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adminReservationRequest)
                .when()
                .post("/admin/reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .body("date", equalTo(DATE),
                        "theme.id", equalTo(1),
                        "time.id", equalTo(1),
                        "member.id", equalTo(1));
    }
}
