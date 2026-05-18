package roomescape.reservation.presentation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
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
class ReservationApiTest {

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
    @DisplayName("예약을 생성하면 201과 생성된 예약을 반환한다")
    void createReservation() {
        Long themeId = createTheme();
        Long timeId = createTime();

        Map<String, Object> body = new HashMap<>();
        body.put("name", "포비");
        body.put("date", "2026-12-31");
        body.put("timeId", timeId);
        body.put("themeId", themeId);

        given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("포비"))
                .body("date", equalTo("2026-12-31"))
                .body("time.id", equalTo(timeId.intValue()))
                .body("theme.id", equalTo(themeId.intValue()));
    }

    @Test
    @DisplayName("예약 생성 요청에 이름이 없으면 400과 공통 에러 응답을 반환한다")
    void createReservationWithoutName() {
        Long themeId = createTheme();
        Long timeId = createTime();

        given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "",
                        "date", "2026-12-31",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("message", containsString("이름은 필수입니다."));
    }

    @Test
    @DisplayName("예약 생성 요청의 날짜 형식이 잘못되면 400과 공통 에러 응답을 반환한다")
    void createReservationWithInvalidDateFormat() {
        Long themeId = createTheme();
        Long timeId = createTime();

        given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026/12/31",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("message", equalTo("날짜는 yyyy-MM-dd 형식이어야 합니다."));
    }

    @Test
    @DisplayName("이름 쿼리 파라미터로 조회하면 해당 이름의 예약만 반환한다")
    void getReservationsByName() {
        Long themeId = createTheme();
        Long timeId = createTime();
        Long otherTimeId = createTime("11:00");

        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-12-31",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations");
        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "크론",
                        "date", "2026-12-31",
                        "timeId", otherTimeId,
                        "themeId", themeId
                ))
                .when().post("/reservations");

        given().log().all()
                .queryParam("name", "포비")
                .when().get("/reservations/mine")
                .then().log().all()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].name", equalTo("포비"));
    }

    @Test
    @DisplayName("예약 목록을 조회하면 200과 예약 목록을 반환한다")
    void getReservations() {
        Long themeId = createTheme();
        Long timeId = createTime();

        Map<String, Object> body = new HashMap<>();
        body.put("name", "포비");
        body.put("date", "2026-12-31");
        body.put("timeId", timeId);
        body.put("themeId", themeId);

        given().contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations");

        given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("본인 예약을 삭제하면 204를 반환한다")
    void deleteReservation() {
        Long themeId = createTheme();
        Long timeId = createTime();

        Map<String, Object> body = new HashMap<>();
        body.put("name", "포비");
        body.put("date", "2026-12-31");
        body.put("timeId", timeId);
        body.put("themeId", themeId);

        Long id = given().contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().extract().jsonPath().getLong("id");

        given().log().all()
                .queryParam("name", "포비")
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하면 404와 공통 에러 응답을 반환한다")
    void deleteReservationWhenNotFound() {
        given().log().all()
                .queryParam("name", "포비")
                .when().delete("/reservations/999")
                .then().log().all()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message", equalTo("존재하지 않는 예약ID 입니다."));
    }

    @Test
    @DisplayName("본인이 아닌 사람이 예약을 삭제하면 404와 공통 에러 응답을 반환한다")
    void deleteReservationByOtherOwner() {
        Long themeId = createTheme();
        Long timeId = createTime();

        Long id = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-12-31",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().extract().jsonPath().getLong("id");

        given().log().all()
                .queryParam("name", "크론")
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message", equalTo("본인의 예약만 취소할 수 있습니다."));
    }

    @Test
    @DisplayName("이미 지난 예약을 취소하면 409와 공통 에러 응답을 반환한다")
    void cancelPastReservation() {
        Long themeId = createTheme();
        Long timeId = createTime();

        Long id = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-12-31",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().extract().jsonPath().getLong("id");

        Instant afterReservation = LocalDateTime.of(2027, 1, 1, 0, 0)
                .atZone(ASIA_SEOUL)
                .toInstant();
        when(clock.instant()).thenReturn(afterReservation);

        given().log().all()
                .queryParam("name", "포비")
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(409)
                .body("status", equalTo(409))
                .body("message", equalTo("이미 지난 예약은 취소할 수 없습니다."));
    }

    @Test
    @DisplayName("중복 예약을 생성하면 409와 공통 에러 응답을 반환한다")
    void createDuplicateReservation() {
        Long themeId = createTheme();
        Long timeId = createTime();

        Map<String, Object> body = new HashMap<>();
        body.put("name", "포비");
        body.put("date", "2026-12-31");
        body.put("timeId", timeId);
        body.put("themeId", themeId);

        given().contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations");

        given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("status", equalTo(409))
                .body("message", equalTo("이미 해당 시간에 예약이 존재합니다."));
    }

    @Test
    @DisplayName("지나간 날짜로 예약을 생성하면 409와 공통 에러 응답을 반환한다")
    void createReservationInPastDate() {
        Long themeId = createTheme();
        Long timeId = createTime();

        given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-05-11",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("status", equalTo(409))
                .body("message", equalTo("지나간 날짜/시간으로는 예약할 수 없습니다."));
    }

    @Test
    @DisplayName("오늘 지난 시간으로 예약을 생성하면 409와 공통 에러 응답을 반환한다")
    void createReservationInPastTimeToday() {
        Long themeId = createTheme();
        Long timeId = createTime("09:00");

        given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-05-12",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("status", equalTo(409))
                .body("message", equalTo("지나간 날짜/시간으로는 예약할 수 없습니다."));
    }

    @Test
    @DisplayName("본인 예약의 날짜와 시간을 변경하면 200과 변경된 예약을 반환한다")
    void updateReservation() {
        Long themeId = createTheme();
        Long timeId = createTime();
        Long newTimeId = createTime("11:00");

        Long id = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-12-31",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().extract().jsonPath().getLong("id");

        given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2027-01-15",
                        "timeId", newTimeId
                ))
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(200)
                .body("id", equalTo(id.intValue()))
                .body("date", equalTo("2027-01-15"))
                .body("time.id", equalTo(newTimeId.intValue()));
    }

    @Test
    @DisplayName("본인이 아닌 사람이 예약을 변경하면 404와 공통 에러 응답을 반환한다")
    void updateReservationByOtherOwner() {
        Long themeId = createTheme();
        Long timeId = createTime();
        Long newTimeId = createTime("11:00");

        Long id = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-12-31",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().extract().jsonPath().getLong("id");

        given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "크론",
                        "date", "2027-01-15",
                        "timeId", newTimeId
                ))
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message", equalTo("본인의 예약만 변경할 수 있습니다."));
    }

    @Test
    @DisplayName("이미 지난 예약을 변경하면 409와 공통 에러 응답을 반환한다")
    void updatePastReservation() {
        Long themeId = createTheme();
        Long timeId = createTime();
        Long newTimeId = createTime("11:00");

        Long id = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-12-31",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().extract().jsonPath().getLong("id");

        Instant afterReservation = LocalDateTime.of(2027, 1, 1, 0, 0)
                .atZone(ASIA_SEOUL)
                .toInstant();
        when(clock.instant()).thenReturn(afterReservation);

        given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2027-02-01",
                        "timeId", newTimeId
                ))
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(409)
                .body("status", equalTo(409))
                .body("message", equalTo("이미 지난 예약은 변경할 수 없습니다."));
    }

    @Test
    @DisplayName("변경하려는 슬롯에 다른 예약이 있으면 409와 공통 에러 응답을 반환한다")
    void updateReservationToDuplicateSlot() {
        Long themeId = createTheme();
        Long timeId = createTime();
        Long otherTimeId = createTime("11:00");

        Long myReservationId = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-12-31",
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().extract().jsonPath().getLong("id");

        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "크론",
                        "date", "2026-12-31",
                        "timeId", otherTimeId,
                        "themeId", themeId
                ))
                .when().post("/reservations");

        given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", "2026-12-31",
                        "timeId", otherTimeId
                ))
                .when().patch("/reservations/" + myReservationId)
                .then().log().all()
                .statusCode(409)
                .body("status", equalTo(409))
                .body("message", equalTo("이미 해당 시간에 예약이 존재합니다."));
    }

    private Long createTheme() {
        return given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "테마A",
                        "description", "설명",
                        "thumbnailImageUrl", "https://example.com/a.png",
                        "durationTime", "01:00:00"
                ))
                .when().post("/admin/themes")
                .then().extract().jsonPath().getLong("id");
    }

    private Long createTime() {
        return createTime("10:00");
    }

    private Long createTime(String startAt) {
        return given().contentType(ContentType.JSON)
                .body(Map.of("startAt", startAt))
                .when().post("/times")
                .then().extract().jsonPath().getLong("id");
    }
}
