package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.fixture.MemberFixture;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ReservationTimeFixture;
import roomescape.fixture.ThemeFixture;
import roomescape.fixture.TokenFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReservationTimeApiControllerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("TRUNCATE TABLE reservation");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE reservation_time");
        jdbcTemplate.update("TRUNCATE TABLE theme");
        jdbcTemplate.update("TRUNCATE TABLE member");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("시간 생성에 성공하면, 201을 반환한다.")
    @Test
    void return_201_when_reservationTime_create_success() {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then()
                .statusCode(201);
    }

    @DisplayName("시간 생성 시 유효하지 않은 시작 시간으로 시간 생성하면 400을 반환한다.")
    @Test
    void return_400_when_reservationTime_create_input_is_invalid() {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", "");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then()
                .statusCode(400);
    }

    @DisplayName("중복된 예약 시간을 생성하려 하면 400을 반환한다.")
    @Test
    void return_400_when_duplicate_reservationTime() {
        ReservationTimeFixture.createAndReturnId("10:00");

        final Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then()
                .statusCode(400);
    }

    @DisplayName("예약 시간 조회에 성공하면, 200을 반환한다.")
    @Test
    void return_200_when_get_reservationTimes_success() {
        ReservationTimeFixture.createAndReturnId("10:00");
        ReservationTimeFixture.createAndReturnId("11:00");

        RestAssured.given()
                .when().get("/times")
                .then()
                .statusCode(200);
    }

    @DisplayName("예약 가능한 시간 조회에 성공하면 200을 반환한다.")
    @Test
    void return_200_when_get_available_reservationTimes_success() {
        final Long timeId = ReservationTimeFixture.createAndReturnId("10:00");
        final Long themeId = ThemeFixture.createAndReturnId("테마 1");
        final Long memberId = MemberFixture.createAndReturnId();

        ReservationFixture.createAndReturnId("2024-06-01", timeId, themeId);

        ReservationTimeFixture.createAndReturnId("11:00");
        ReservationTimeFixture.createAndReturnId("12:00");

        RestAssured.given()
                .when().get("/times/available?date=2024-06-01&themeId=" + themeId)
                .then()
                .statusCode(200);
    }

    @DisplayName("시간 삭제에 성공하면, 204를 반환한다.")
    @Test
    void return_204_when_reservationTime_delete_success() {
        final Long timeId = ReservationTimeFixture.createAndReturnId("10:00");

        RestAssured.given()
                .delete("/times/" + timeId)
                .then()
                .statusCode(204);
    }

    @DisplayName("특정 시간이 존재하지 않는데, 그 시간을 삭제하려 할 때 400을 반환한다.")
    @Test
    void return_404_when_not_exist_id() {
        RestAssured.given()
                .delete("/times/-1")
                .then()
                .statusCode(400);
    }

    @DisplayName("특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때 400을 반환한다.")
    @Test
    void return_400_when_delete_id_that_exist_reservation() {
        final Long timeId = ReservationTimeFixture.createAndReturnId("10:00");
        final Long themeId = ThemeFixture.createAndReturnId("테마 1");
        final Long memberId = MemberFixture.createAndReturnId();

        ReservationFixture.createAndReturnId("2024-06-01", timeId, themeId);

        final String token = TokenFixture.getToken();

        RestAssured.given()
                .cookie("access_token", token)
                .delete("/times/" + timeId)
                .then()
                .statusCode(400);
    }
}
