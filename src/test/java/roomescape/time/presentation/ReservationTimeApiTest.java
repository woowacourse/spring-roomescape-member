package roomescape.time.presentation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeApiTest {

    private static final ZoneId ASIA_SEOUL = ZoneId.of("Asia/Seoul");
    private static final Instant FIXED_INSTANT = LocalDateTime.of(2026, 5, 12, 10, 0)
            .atZone(ASIA_SEOUL)
            .toInstant();

    @LocalServerPort
    private int port;

    @MockBean
    private Clock clock;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        when(clock.getZone()).thenReturn(ASIA_SEOUL);
        when(clock.instant()).thenReturn(FIXED_INSTANT);
    }

    @Test
    @DisplayName("예약 시간을 생성하면 201과 생성된 시간을 반환한다")
    void createReservationTime() {
        given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("startAt", equalTo("10:00:00"));
    }

    @Test
    @DisplayName("예약 시간 목록을 조회하면 200과 시간 목록을 반환한다")
    void getReservationTimes() {
        given().contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/times");
        given().contentType(ContentType.JSON)
                .body(Map.of("startAt", "11:00"))
                .when().post("/times");

        given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("times.size()", equalTo(2));
    }

    @Test
    @DisplayName("예약 시간을 삭제하면 204를 반환한다")
    void deleteReservationTime() {
        Long id = given().contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/times")
                .then().extract().jsonPath().getLong("id");

        given().log().all()
                .when().delete("/times/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("예약이 있는 시간을 삭제하면 409와 공통 에러 응답을 반환한다")
    void deleteReservationTimeInUse() {
        Long themeId = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "테마A",
                        "description", "설명",
                        "thumbnailImageUrl", "https://example.com/a.png",
                        "durationTime", "01:00:00"
                ))
                .when().post("/admin/themes")
                .then().extract().jsonPath().getLong("id");

        Long timeId = given().contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/times")
                .then().extract().jsonPath().getLong("id");

        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-12-31",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations");

        given().log().all()
                .when().delete("/times/" + timeId)
                .then().log().all()
                .statusCode(409)
                .body("status", equalTo(409))
                .body("message", equalTo("해당 시간에 예약이 존재합니다."));
    }

    @Test
    @DisplayName("특정 테마와 날짜의 예약 가능 시간을 조회하면 200과 가용 시간을 반환한다")
    void getAvailableReservationTime() {
        Long themeId = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "테마A",
                        "description", "설명",
                        "thumbnailImageUrl", "https://example.com/a.png",
                        "durationTime", "01:00:00"
                ))
                .when().post("/admin/themes")
                .then().extract().jsonPath().getLong("id");

        given().contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/times");
        given().contentType(ContentType.JSON)
                .body(Map.of("startAt", "11:00"))
                .when().post("/times");

        given().log().all()
                .queryParam("themeId", themeId)
                .queryParam("date", "2026-12-31")
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .body("theme.id", equalTo(themeId.intValue()))
                .body("times", hasSize(2));
    }

    @Test
    @DisplayName("오늘 예약 가능 시간을 조회하면 이미 지난 시간은 제외한다")
    void getAvailableReservationTimeWithoutPastTimesToday() {
        Long themeId = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "테마A",
                        "description", "설명",
                        "thumbnailImageUrl", "https://example.com/a.png",
                        "durationTime", "01:00:00"
                ))
                .when().post("/admin/themes")
                .then().extract().jsonPath().getLong("id");

        given().contentType(ContentType.JSON)
                .body(Map.of("startAt", "09:00"))
                .when().post("/times");
        given().contentType(ContentType.JSON)
                .body(Map.of("startAt", "11:00"))
                .when().post("/times");

        given().log().all()
                .queryParam("themeId", themeId)
                .queryParam("date", "2026-05-12")
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .body("theme.id", equalTo(themeId.intValue()))
                .body("times", hasSize(1))
                .body("times[0].startAt", equalTo("11:00:00"));
    }
}
