package roomescape.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ReservationIntegrationTest extends IntegrationTest {
    @Test
    void 예약_목록을_조회할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 예약을_추가할_수_있다() {
        Map<String, String> params = new HashMap<>(); // TODO: nested 써서 중복 코드 제거하기
        params.put("name", "브라운");
        params.put("date", "2023-08-06");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/2")
                .body("id", is(2));
    }

    @Test
    void 시간이_빈_값이면_예약을_추가할_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-06");
        params.put("timeId", null);
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 날짜의_형식이_다르면_예약을_추가할_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-13-05");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 시간대와_테마가_똑같은_중복된_예약은_추가할_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "도라");
        params.put("date", "2023-08-05");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void 지나간_날짜와_시간에_대한_예약은_추가할_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "도라");
        params.put("date", "1998-12-11");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 예약을_삭제할_수_있다() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isZero();
    }

    @Test
    void 존재하지_않는_예약은_삭제할_수_없다() {
        RestAssured.given().log().all()
                .when().delete("/reservations/10")
                .then().log().all()
                .statusCode(404);
    }
}
