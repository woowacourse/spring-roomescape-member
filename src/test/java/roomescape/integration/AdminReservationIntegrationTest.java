package roomescape.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql({"/schema.sql", "/data.sql"})
class AdminReservationIntegrationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = this.port;
    }

    @Test
    void addAdminReservationTest() {
        final Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "admin@email.com");
        credentials.put("password", "password");

        final String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().cookie("token");

        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", LocalDate.now().plusDays(1).toString());
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);
        reservation.put("memberId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .cookie("token", token)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void findByThemeAndMemberAndDateTest() {
        final Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "admin@email.com");
        credentials.put("password", "password");

        final String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().cookie("token");

        final String requestParamUrl = "?themeId=1&memberId=1&dateFrom=2025-04-25&dateTo=2025-04-27";

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/admin/search" + requestParamUrl)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
