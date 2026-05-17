package roomescape.reservation.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/create_reservation_time.sql", "/create_theme.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminReservationControllerTest {

    @Test
    void 관리자가_예약_목록을_조회한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2099-05-03");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("name", hasItem("밀란"))
                .body("date", hasItem("2099-05-03"))
                .body("time.id", hasItem(1));
    }

    @Test
    void 관리자가_예약을_삭제한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2099-05-03");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().delete("/admin/reservations/" + id)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 관리자가_존재하지_않는_예약을_삭제하면_404를_응답한다() {
        RestAssured.given().log().all()
                .when().delete("/admin/reservations/999")
                .then().log().all()
                .statusCode(404)
                .body("message", is("존재하지 않는 예약입니다. id=999"));
    }
}
