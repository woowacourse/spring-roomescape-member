package roomescape;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MissionStep3Test {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 데이터베이스_연동() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 시간_조회_API() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        RestAssured.given().log().all()
                .when().get("/user/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(12));
    }

//    @Test
//    void 시간_추가_API() {
//        Map<String, String> params = new HashMap<>();
//        params.put("startAt", "21:00");
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/user/times")
//                .then().log().all()
//                .statusCode(200);
//
//        RestAssured.given().log().all()
//                .when().get("/user/times")
//                .then().log().all()
//                .statusCode(200)
//                .body("size()", is(12));
//    }
//
//    @Test
//    void 시간_관리_API() {
//        Map<String, String> params = new HashMap<>();
//        params.put("startAt", "10:00");
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/user/times")
//                .then().log().all()
//                .statusCode(200);
//
//        RestAssured.given().log().all()
//                .when().get("/user/times")
//                .then().log().all()
//                .statusCode(200)
//                .body("size()", is(1));
//
//        RestAssured.given().log().all()
//                .when().delete("/user/times/1")
//                .then().log().all()
//                .statusCode(200);
//    }
//
//    @Test
//    void 예약과_시간_연결() {
//        Map<String, String> params = new HashMap<>();
//        params.put("startAt", "10:00");
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/user/times")
//                .then().log().all()
//                .statusCode(200);
//
//        Map<String, Object> reservation = new HashMap<>();
//        reservation.put("name", "브라운");
//        reservation.put("date", "2026-05-05");
//        reservation.put("timeId", 1);
//        reservation.put("themeId", 1);
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(reservation)
//                .when().post("/user/reservations")
//                .then().log().all()
//                .statusCode(201);
//
//        RestAssured.given().log().all()
//                .when().get("/user/reservations")
//                .then().log().all()
//                .statusCode(200)
//                .body("size()", is(1));
//    }
}