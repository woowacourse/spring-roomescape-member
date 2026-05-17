package roomescape.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationScenarioTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('무서운 이야기', '공포', 'http://example.com')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");
    }

    @Test
    @DisplayName("예약을 생성하면 해당 이름으로 내 예약을 조회할 수 있다.")
    void 예약_생성_후_내_예약_조회() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-08-04");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations/mine?name=브라운")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약을 수정하면 변경된 이름으로 내 예약을 조회할 수 있다.")
    void 예약_수정_후_변경된_이름으로_조회() {
        Map<String, String> createParams = new HashMap<>();
        createParams.put("name", "브라운");
        createParams.put("date", "2026-08-04");
        createParams.put("timeId", "1");
        createParams.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(createParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("name", "은오");
        updateParams.put("date", "2026-08-08");
        updateParams.put("timeId", "1");
        updateParams.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateParams)
                .when().put("/reservations/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations/mine?name=은오")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약을 수정하면 이전 이름으로는 내 예약을 조회할 수 없다.")
    void 예약_수정_후_이전_이름으로_조회시_빈_결과() {
        Map<String, String> createParams = new HashMap<>();
        createParams.put("name", "브라운");
        createParams.put("date", "2026-08-04");
        createParams.put("timeId", "1");
        createParams.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(createParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("name", "은오");
        updateParams.put("date", "2026-08-08");
        updateParams.put("timeId", "1");
        updateParams.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateParams)
                .when().put("/reservations/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations/mine?name=브라운")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("예약을 삭제하면 내 예약 목록에서 제거된다.")
    void 예약_생성_후_삭제() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-08-04");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations/mine?name=브라운")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("예약하면 해당 날짜와 테마의 가용 시간에서 예약된 시간이 제외된다.")
    void 예약_후_해당_시간은_예약_가능_시간에서_제외됨() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('11:00')");

        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-06-04");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times?themeId=1&date=2026-06-04")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
