package roomescape.core.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.core.dto.auth.TokenRequest;
import roomescape.core.dto.reservation.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class AdminControllerTest {
    private static final String TOMORROW_DATE = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE);
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "password";

    private String accessToken;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        accessToken = RestAssured
                .given().log().all()
                .body(new TokenRequest(EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().cookies().extract().cookie("token");
    }

    @Test
    @DisplayName("관리자 페이지로 이동한다.")
    void moveToAdminPage() {
        RestAssured.given().log().all()
                .cookies("token", accessToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 관리 페이지로 이동한다.")
    void moveToReservationManagePage() {
        RestAssured.given().log().all()
                .cookies("token", accessToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("테마 관리 페이지로 이동한다.")
    void moveToThemeManagePage() {
        RestAssured.given().log().all()
                .cookies("token", accessToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("시간 관리 페이지로 이동한다.")
    void moveToTimeManagePage() {
        RestAssured.given().log().all()
                .cookies("token", accessToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약자를 지정해서 예약을 생성할 수 있다.")
    void createReservationAsAdmin() {
        ReservationRequest request = new ReservationRequest(2L,
                TOMORROW_DATE, 1L, 1L);

        RestAssured.given().log().all()
                .cookies("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }
}
