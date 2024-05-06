package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.dto.request.ThemeRequest;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ThemeTest {

    @LocalServerPort
    int port;

    @MockBean
    private Clock clock;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        given(clock.instant()).willReturn(Instant.parse("2024-05-02T19:19:00Z"));
        given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));
    }

    @DisplayName("테마 추가 API 테스트")
    @Test
    void createReservation() {
        ThemeRequest themeRequest = new ThemeRequest("happy", "hi", "abcd.html");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    @DisplayName("테마 조회 API 테스트")
    @Test
    void getReservation() {
        ThemeRequest themeRequest = new ThemeRequest("happy", "hi", "abcd.html");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("테마 삭제 API 테스트")
    @Test
    void deleteReservation() {
        ThemeRequest themeRequest = new ThemeRequest("happy", "hi", "abcd.html");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("주간 상위 10개 예약 테마 조회 API 테스트")
    @Sql("/reservationData.sql")
    @Test
    void weeklyTop10Theme() {
        RestAssured.given().log().all()
                .when().get("/themes/ranking")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10));
    }
}
