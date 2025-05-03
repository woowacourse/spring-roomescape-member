package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.ReservationTime;
import roomescape.repository.impl.JdbcReservationTimeRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 일단계() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 이단계() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0)); // 아직 생성 요청이 없으니 Controller에서 임의로 넣어준 Reservation 갯수 만큼 검증하거나 0개임을 확인하세요.
    }

    @Test
    void 사단계() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

//    @Test
//    void 칠단계() {
//        Map<String, String> params = new HashMap<>();
//        params.put("startAt", "10:00");
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/times")
//                .then().log().all()
//                .statusCode(200);
//
//        RestAssured.given().log().all()
//                .when().get("/times")
//                .then().log().all()
//                .statusCode(200)
//                .body("size()", is(1));
//
//        RestAssured.given().log().all()
//                .when().delete("/times/1")
//                .then().log().all()
//                .statusCode(200);
//    }
//
//    @Test
//    void 팔단계() {
//        Map<String, Object> reservation = new HashMap<>();
//        reservation.put("name", "브라운");
//        reservation.put("date", "2023-08-05");
//        reservation.put("timeId", 1);
//
//        new JdbcReservationTimeRepository(jdbcTemplate).saveReservationTime(new ReservationTime(1L, LocalTime.now()));
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(reservation)
//                .when().post("/reservations")
//                .then().log().all()
//                .statusCode(200);
//
//
//        RestAssured.given().log().all()
//                .when().get("/reservations")
//                .then().log().all()
//                .statusCode(200)
//                .body("size()", is(1));
//    }
}

