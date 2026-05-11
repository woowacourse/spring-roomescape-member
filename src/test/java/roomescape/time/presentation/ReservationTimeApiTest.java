package roomescape.time.presentation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
class ReservationTimeApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
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
                .body("size()", equalTo(2));
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
}
