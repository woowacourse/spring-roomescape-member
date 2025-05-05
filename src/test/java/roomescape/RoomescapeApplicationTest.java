package roomescape;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomescapeApplicationTest {

    @Test
    @DisplayName("예약을 정상적으로 추가한다")
    public void test1() {
        String requestBody = "{"
                + "\"name\": \"홍미미\","
                + "\"date\": \"2025-05-10\","
                + "\"timeId\": 1,"
                + "\"themeId\": 1"
                + "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/reservations")
                .then()
                .statusCode(201)
                .body("name", equalTo("홍미미"))
                .body("date", equalTo("2025-05-10"))
                .body("theme.id", equalTo(1))
                .body("theme.name", equalTo("theme1"))
                .body("theme.description", equalTo("description1"))
                .body("theme.thumbnail", equalTo("thumbnail1"))
                .body("time.id", equalTo(1))
                .body("time.startAt", equalTo("10:00"))
                .extract().response();
    }

    @Test
    @DisplayName("모든 예약을 조회한다")
    public void test2() {
        RestAssured.given()
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", equalTo(71));
    }

    @Test
    @DisplayName("예약을 삭제한다")
    public void test3() {
        Long deleteId = 1L;

        RestAssured.given()
                .pathParam("id", deleteId)
                .when()
                .delete("/reservations/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 테마 id로 예약을 추가하면 예외를 응답한다")
    public void test4() {
        String requestBody = "{"
                + "\"name\": \"홍미미\","
                + "\"date\": \"2025-05-10\","
                + "\"timeId\": 1,"
                + "\"themeId\": 999"
                + "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/reservations")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 테마 id가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("존재하지 않는 시간 id로 예약을 추가하면 예외를 응답한다")
    public void test5() {
        String requestBody = "{"
                + "\"name\": \"홍미미\","
                + "\"date\": \"2025-05-10\","
                + "\"timeId\": 999, "
                + "\"themeId\": 1"
                + "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/reservations")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 시간 id가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("이미 예약된 날짜, 시간, 테마로 예약을 시도하면 예외를 응답한다")
    public void test6() {
        String requestBody = "{"
                + "\"name\": \"홍미미\","
                + "\"date\": \"2025-05-10\","
                + "\"timeId\": 1,"
                + "\"themeId\": 1"
                + "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/reservations")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/reservations")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 이미 예약이 존재합니다."));
    }

    @Test
    @DisplayName("존재하지 않는 예약 id로 삭제를 시도하면 예외를 응답한다")
    public void test7() {
        Long invalidId = 999L;

        RestAssured.given()
                .pathParam("id", invalidId)
                .when()
                .delete("/reservations/{id}")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 해당 id의 예약이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("새로운 테마를 정상적으로 추가한다")
    public void test10() {
        String requestBody = "{"
                + "\"name\": \"테마1\","
                + "\"description\": \"테마 설명\","
                + "\"thumbnail\": \"테마 썸네일\""
                + "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/themes")
                .then()
                .statusCode(201)
                .body("name", equalTo("테마1"))
                .body("description", equalTo("테마 설명"))
                .body("thumbnail", equalTo("테마 썸네일"));
    }

    @Test
    @DisplayName("모든 테마를 정상적으로 조회한다")
    public void test11() {
        RestAssured.given()
                .when()
                .get("/themes")
                .then()
                .statusCode(200)
                .body("size()", equalTo(14));
    }

    @Test
    @DisplayName("테마를 정상적으로 삭제한다")
    public void test12() {
        Long themeId = 14L;

        RestAssured.given()
                .pathParam("id", themeId)
                .when()
                .delete("/themes/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("이미 존재하는 테마를 추가할 시 예외를 응답한다")
    public void test13() {
        String requestBody = "{"
                + "\"name\": \"theme1\","
                + "\"description\": \"중복된 테마 이름을 추가해볼게요\","
                + "\"thumbnail\": \"테마 이름이 중복이면 중복인 것입니다\""
                + "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/themes")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 이미 테마가 존재합니다."));
    }

    @Test
    @DisplayName("예약된 테마를 삭제하려 할 때 예외를 응답한다")
    public void test14() {
        Long reservedThemeId = 1L;

        RestAssured.given()
                .pathParam("id", reservedThemeId)
                .when()
                .delete("/themes/{id}")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 예약된 테마의 id는 삭제할 수 없습니다."));
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하려 할 때 예외를 응답한다")
    public void test15() {
        Long invalidThemeId = 999L;

        RestAssured.given()
                .pathParam("id", invalidThemeId)
                .when()
                .delete("/themes/{id}")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 테마 id가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("인기 테마를 조회할 수 있다")
    void test16() {
        RestAssured.given()
                .when()
                .get("/themes/popular/weekly")
                .then()
                .statusCode(200)
                .body("[0].name", equalTo("theme11"))
                .body("[9].name", equalTo("theme2"));
    }

    @Test
    @DisplayName("시간을 정상적으로 추가한다")
    public void test21() {
        String requestBody = "{"
                + "\"startAt\": \"09:00\""
                + "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/times")
                .then()
                .statusCode(201)
                .body("startAt", equalTo("09:00"));
    }

    @Test
    @DisplayName("모든 시간을 조회한다")
    public void test22() {
        RestAssured.given()
                .when()
                .get("/times")
                .then()
                .statusCode(200)
                .body("size()", equalTo(13));
    }

    @Test
    @DisplayName("존재하지 않는 테마 id로 시간 조회를 시도하면 예외를 응답한다")
    public void test23() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam("date", "2025-05-10")
                .queryParam("themeId", 999)
                .when()
                .get("/times/available")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 테마 id가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("중복된 시간을 추가하면 예외를 응답한다")
    public void test24() {
        String requestBody = "{"
                + "\"startAt\": \"10:00\""
                + "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/times")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 이미 해당 시간이 존재합니다."));
    }

    @Test
    @DisplayName("시간을 삭제한다")
    public void test25() {
        Long deleteId = 13L;

        RestAssured.given()
                .pathParam("id", deleteId)
                .when()
                .delete("/times/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 시간 id로 삭제를 시도하면 예외를 응답한다")
    public void test26() {
        Long invalidId = 999L;

        RestAssured.given()
                .pathParam("id", invalidId)
                .when()
                .delete("/times/{id}")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 해당 id의 시간이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("예약된 시간은 삭제할 수 없다")
    public void test27() {
        Long reservedTimeId = 1L;

        RestAssured.given()
                .pathParam("id", reservedTimeId)
                .when()
                .delete("/times/{id}")
                .then()
                .statusCode(400)
                .body(equalTo("[ERROR] 예약된 시간은 삭제할 수 없습니다."));
    }
}
