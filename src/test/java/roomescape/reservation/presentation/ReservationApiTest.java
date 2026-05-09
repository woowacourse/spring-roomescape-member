package roomescape.reservation.presentation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
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
    @DisplayName("예약을 삭제하면 204를 반환한다")
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
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);
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
        return given().contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/times")
                .then().extract().jsonPath().getLong("id");
    }
}
