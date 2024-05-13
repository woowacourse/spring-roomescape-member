package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/truncate.sql", "/memberData.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ReservationControllerTest {

    @LocalServerPort
    private int port;

    private final Map<String, String> memberParams = Map.of(
            "email", "ddang@google.com",
            "password", "password"
    );

    private final Map<String, String> timeParams = Map.of("startAt", "17:00");

    private final Map<String, String> themeParams = Map.of(
            "name", "테마명",
            "description", "설명",
            "thumbnail", "썸네일 URL"
    );

    private final Map<String, String> reservationParams = Map.of(
            "date", LocalDate.now().plusDays(1L).toString(),
            "timeId", "1",
            "themeId", "1"
    );

    @BeforeEach
    void setUp() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(timeParams)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(themeParams)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    @Test
    @DisplayName("처음으로 등록하는 예약의 id는 1이다.")
    void firstPost() {
        String token = loginWith(memberParams);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .port(port)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .header("Location", "/reservations/1");
    }

    @Test
    @DisplayName("아무 예약도 하지 않은 경우, 예약 목록 조회 결과 개수는 0개이다.")
    void readEmptyReservations() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("하나의 예약만 등록한 경우, 예약 목록 조회 결과 개수는 1개이다.")
    void readReservationsSizeAfterFirstPost() {
        String token = loginWith(memberParams);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .cookie("token", token)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .header("Location", "/reservations/1");

        RestAssured.given().log().all()
                .port(port)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("하나의 예약만 등록한 경우, 예약 삭제 뒤 예약 목록 조회 결과 개수는 0개이다.")
    void readReservationsSizeAfterPostAndDelete() {
        String token = loginWith(memberParams);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .port(port)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .header("Location", "/reservations/1");

        RestAssured.given().log().all()
                .port(port)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .port(port)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("특정 날짜의 특정 테마 예약 현황을 조회한다.")
    void readReservationByDateAndThemeId() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .when().get("/reservations/themes/1?date=" + LocalDate.MAX)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    private String loginWith(Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().cookie("token");
    }
}
