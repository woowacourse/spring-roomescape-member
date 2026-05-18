package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReservationAcceptanceTest extends AcceptanceTestSupport{

    @Test
    @DisplayName("예약 삭제 테스트")
    void deleteReservationTest() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (1, '공포의 저택', 'url1', '설명1', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO reservation
            VALUES (1, 'user_a', '2026-04-28', 1, 1)
            """);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .queryParam("name", "user_a")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("이름으로 예약을 조회하면 200 상태 코드와 예약 내역들을 반환한다.")
    void findReservationsByNameTest() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);

        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (1, '우주 탐험대', 'https://picsum.photos/seed/space/400/300', '은하계를 누비는 우주 탐험', 'AVAILABLE')
            """);

        LocalDate reservedDate = LocalDate.now().plusDays(1);
        String name = "타스";

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", reservedDate);
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .queryParam("name", name)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("타스"))
                .body("[0].date", is(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(reservedDate)))
                .body("[0].time.id", is(1))
                .body("[0].time.startAt", is("10:00"))
                .body("[0].theme.id", is(1))
                .body("[0].theme.name", is("우주 탐험대"))
                .body("[0].theme.thumbnailUrl", is("https://picsum.photos/seed/space/400/300"))
                .body("[0].theme.description", is("은하계를 누비는 우주 탐험"));
    }

    @Test
    @DisplayName("id로 예약을 조회하면 200 상태 코드와 예약 내역을 반환한다.")
    void findReservationByIdTest() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);

        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (1, '우주 탐험대', 'https://picsum.photos/seed/space/400/300', '은하계를 누비는 우주 탐험', 'AVAILABLE')
            """);

        LocalDate reservedDate = LocalDate.now().plusDays(1);
        String name = "타스";

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", reservedDate);
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        long id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201).extract().jsonPath().getLong("id");

        RestAssured.given().log().all()
                .when().get("/reservations/" + id)
                .then().log().all()
                .statusCode(200)
                .body("name", is("타스"))
                .body("date", is(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(reservedDate)))
                .body("time.id", is(1))
                .body("time.startAt", is("10:00"))
                .body("theme.id", is(1))
                .body("theme.name", is("우주 탐험대"))
                .body("theme.thumbnailUrl", is("https://picsum.photos/seed/space/400/300"))
                .body("theme.description", is("은하계를 누비는 우주 탐험"));
    }


    @Test
    @DisplayName("예약 날짜와 시간을 수정하면 200 상태코드와 수정된 예약을 반환한다.")
    void updateReservationTest() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (2, '14:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (1, '우주 탐험대', 'https://picsum.photos/seed/space/400/300', '은하계를 누비는 우주 탐험', 'AVAILABLE')
            """);

        LocalDate originalDate = LocalDate.now().plusDays(1);
        LocalDate newDate = LocalDate.now().plusDays(2);

        Map<String, Object> createParams = new HashMap<>();
        createParams.put("name", "타스");
        createParams.put("date", originalDate);
        createParams.put("timeId", 1L);
        createParams.put("themeId", 1L);

        long id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createParams)
                .when().post("/reservations")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("date", newDate);
        updateParams.put("timeId", 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateParams)
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(200)
                .body("date", is(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(newDate)))
                .body("time.startAt", is("14:00"));
    }

    @Test
    @DisplayName("수정 시 지난 날짜로 변경하면 400 상태코드를 반환한다.")
    void updateReservationWithPastDateTest() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (1, '우주 탐험대', 'https://picsum.photos/seed/space/400/300', '은하계를 누비는 우주 탐험', 'AVAILABLE')
            """);

        LocalDate originalDate = LocalDate.now().plusDays(1);

        Map<String, Object> createParams = new HashMap<>();
        createParams.put("name", "타스");
        createParams.put("date", originalDate);
        createParams.put("timeId", 1L);
        createParams.put("themeId", 1L);

        long id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createParams)
                .when().post("/reservations")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("date", LocalDate.now().minusDays(1));
        updateParams.put("timeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateParams)
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약자 명으로 예약 조회 시 예약자 이름이 누락된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void blankNameWhenFindReservationsByNameTest() {
        RestAssured.given().log().all()
                .queryParam("name", "")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("예약자 이름은 필수입니다."));
    }

    @Test
    @DisplayName("예약 요청에 예약자 이름이 누락된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void blankNameReservationRequestTest() {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("예약자 이름은 필수입니다."));
    }

    @Test
    @DisplayName("예약 요청에 예약 날짜가 누락된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void nullDateReservationRequestTest() {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "타스");
        params.put("date", null);
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("예약 날짜는 필수입니다."));
    }

    @Test
    @DisplayName("예약 요청에 지난 날짜가 예약 날짜로 입력된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void pastDateReservationRequestTest() {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "타스");
        params.put("date", LocalDate.now().minusDays(1).toString());
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("지나간 날짜에 대한 예약 생성은 불가능합니다."));
    }
}
