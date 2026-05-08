package roomescape.reservation;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ThemeFixture;
import roomescape.support.TestDataHelper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private TestDataHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new TestDataHelper(jdbcTemplate);
        testHelper.clearDatabase();
    }

    @DisplayName("방탈출 예약 추가 API를 테스트합니다.")
    @Test
    void save_reservation() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(9, 0));

        Map<String, String> params = ReservationFixture.futureReservationParams(themeId, timeId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", greaterThan(0))
                .body("name", equalTo("스타크"))
                .body("date", equalTo("2028-05-06"))
                .body("time.id", equalTo(timeId.intValue()))
                .body("time.startAt", equalTo("09:00"))
                .body("theme.id", equalTo(themeId.intValue()))
                .body("theme.name", equalTo("공포 테마"));
    }

    @DisplayName("방탈출 예약 삭제 API를 테스트합니다.")
    @Test
    void delete_reservation() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(9, 0));

        Map<String, String> params = ReservationFixture.futureReservationParams(themeId, timeId);

        Integer reservationId = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        RestAssured.given()
                .when().delete("/reservations/{id}", reservationId)
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("과거 날짜로 예약 생성 요청 시 400 응답 반환을 테스트합니다.")
    @Test
    void save_reservation_with_past_date() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(9, 0));

        Map<String, String> params = ReservationFixture.pastReservationParams(themeId, timeId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorMessage", equalTo("현재 시간보다 이전 시간으로 예약을 할 수 없습니다."));
    }

    @DisplayName("예약 생성 요청에서 이름이 비어 있을 시 400 응답 반환을 테스트합니다.")
    @Test
    void save_reservation_with_blank_name() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(9, 0));

        Map<String, String> params = ReservationFixture.futureReservationParams(themeId, timeId);
        params.put("name", "");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorMessage", equalTo("이름은 비어있을 수 없습니다."));
    }

    @DisplayName("예약 생성 요청에서 날짜 형식이 올바르지 않을 시 400 응답 반환을 테스트합니다.")
    @Test
    void save_reservation_with_invalid_date_format() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(9, 0));

        Map<String, String> params = ReservationFixture.futureReservationParams(themeId, timeId);
        params.put("date", "2028/05/06");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorMessage", equalTo("날짜 형식은 yyyy-MM-dd 이어야 합니다."));
    }

    @DisplayName("이미 예약된 날짜와 시간으로 예약 시 400 응답 반환을 테스트합니다.")
    @Test
    void save_duplicated_reservation() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(9, 0));

        Map<String, String> params = ReservationFixture.futureReservationParams(themeId, timeId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorMessage", equalTo("이미 해당 날짜와 시간에 예약이 존재합니다."));
    }

    @DisplayName("존재하지 않는 예약을 삭제 시 404 응답 반환을 테스트합니다.")
    @Test
    void delete_not_existing_reservation() {
        RestAssured.given()
                .when().delete("/reservations/{id}", 999L)
                .then().log().all()
                .statusCode(404);
    }
}
