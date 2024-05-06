package roomescape.web.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.time.LocalDate;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.service.request.ReservationRequest;
import roomescape.support.IntegrationTestSupport;

/*
 * 테스트 데이터베이스 예약 초기 데이터
 * {ID=1, NAME=브라운, DATE=2023-05-04, TIME={ID=1, START_AT="10:00"}, THEME={ID=1, NAME="레벨1 탈출"}}
 * {ID=2, NAME=엘라, DATE=2023-05-04, TIME={ID=2, START_AT="11:00"}, THEME={ID=1, NAME="레벨1 탈출"}}
 * {ID=3, NAME=릴리, DATE=2023-08-05, TIME={ID=2, START_AT="11:00"}, THEME={ID=1, NAME="레벨1 탈출"}}
 */
class ReservationControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("전체 예약 목록을 조회한다.")
    void getAll() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    @DisplayName("예약을 생성한다.")
    void create() {
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationRequest request = new ReservationRequest("브라운", date.toString(), 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", startsWith("/reservations/"))
                .body("name", is("브라운"))
                .body("date", is(date.toString()));
    }

    @Test
    @DisplayName("예약자 이름은 필수이다.")
    void validateName() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약자 이름은 필수입니다."));
    }

    @Test
    @DisplayName("예약 날짜는 필수이다.")
    void validateDate() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약 날짜는 필수입니다."));
    }

    @Test
    @DisplayName("예약 날짜는 올바른 형식이어야 한다.")
    void validateDateFormat() {
        String invalidDate = "date";
        ReservationRequest invalidRequest = new ReservationRequest("name", invalidDate, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("올바른 날짜 형태가 아닙니다."));
    }

    @Test
    @DisplayName("예약 시간 ID는 필수이다.")
    void validateTimeId() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약 시간 ID는 필수입니다."));
    }

    @Test
    @DisplayName("예약 시간 ID는 0보다 커야 한다.")
    void nonPositiveTimeId() {
        long invalidTimeId = 0L;
        ReservationRequest invalidRequest = new ReservationRequest("name", "2023-12-23", invalidTimeId, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("0보다 커야 합니다"));
    }

    /*
     * 테스트 데이터베이스 시간 초기 데이터
     * {ID=1, START_AT=10:00}
     * {ID=2, START_AT=11:00}
     * {ID=3, START_AT=13:00}
     */
    @Test
    @DisplayName("존재하지 않는 시간 ID에 대한 예약을 할 수 없다.")
    void nonExistTimeId() {
        long nonExistTimeId = 4L;
        String date = "2023-12-23";
        ReservationRequest invalidRequest = new ReservationRequest("name", date, nonExistTimeId, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("해당되는 예약 시간이 없습니다."));
    }

    @Test
    @DisplayName("예약 테마 ID는 필수이다.")
    void validateThemeId() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약 테마 ID는 필수입니다."));
    }

    @Test
    @DisplayName("예약 테마 ID는 0보다 커야 한다.")
    void nonPositiveThemeId() {
        long invalidThemeId = 0L;
        ReservationRequest invalidRequest = new ReservationRequest("name", "2023-12-23", 1L, invalidThemeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("0보다 커야 합니다"));
    }

    /*
     * 테스트 데이터베이스 테마 초기 데이터
     * {ID=1, NAME="레벨1 탈출"}
     * {ID=2, NAME="레벨2 탈출"}
     */
    @Test
    @DisplayName("존재하지 않는 테마 ID에 대한 예약을 할 수 없다.")
    void nonExistThemeId() {
        long nonExistThemeId = 3L;
        ReservationRequest invalidRequest = new ReservationRequest("name", "2023-12-23", 1L, nonExistThemeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("해당되는 테마가 없습니다."));
    }

    @Test
    @DisplayName("중복 예약을 생성할 수 없다.")
    void duplicated() {
        String date = LocalDate.now().plusDays(1).toString();
        ReservationRequest request = new ReservationRequest("브라운", date, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("중복된 예약이 존재합니다."));
    }

    @Test
    @DisplayName("지나간 시간에 대한 예약을 할 수 없다.")
    void previousDateTime() {
        String previousDate = LocalDate.now().minusDays(1).toString();
        ReservationRequest request = new ReservationRequest("브라운", previousDate, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("이미 지나간 시간에 대한 예약을 할 수 없습니다."));
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
}
