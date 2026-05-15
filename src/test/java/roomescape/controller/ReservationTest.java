package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/testReservationData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import(FixedClockConfig.class)
public class ReservationTest {

    @Test
    @DisplayName("예약이 성공적으로 테스트가 되는지 확인한다.")
    void createReservationTest() {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "녀녕");
        params.put("date", "2026-06-05");
        params.put("timeId", 1L);
        params.put("themeId", 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[1].id", is(2))
                .body("[1].name", is("녀녕"))
                .body("[1].date", is("2026-06-05"))
                .body("[1].time.id", is(1))
                .body("[1].time.startAt", is("10:00"))
                .body("[1].theme.id", is(2))
                .body("[1].theme.name", is("예약없는테마"))
                .body("[1].theme.thumbnailUrl", is("https://picsum.photos/seed/empty/400/300"))
                .body("[1].theme.description", is("예약이 없는 테마"));
    }

    @Test
    @DisplayName("이전 시간에 대해서는 예약을 생성할 수 없다.")
    void pastReservationTest() {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "녀녕");
        params.put("date", "2026-04-05");
        params.put("timeId", 1L);
        params.put("themeId", 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약이 성공적으로 삭제되는지 확인한다.")
    void deleteReservationTest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
