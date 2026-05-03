package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SecondMissionStepTest {

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

//    @Test
//    void DB_조회_API_전환() {
//        jdbcTemplate.update("INSERT INTO reservation (name, date, time) VALUES (?, ?, ?)", "브라운", "2023-08-05", "15:40");
//
//        List<Reservation> reservations = RestAssured.given().log().all()
//                .when().get("/reservations")
//                .then().log().all()
//                .statusCode(200).extract()
//                .jsonPath().getList(".", Reservation.class);
//
//        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
//
//        assertThat(reservations.size()).isEqualTo(count);
//    }
//
//    @Test
//    void DB_추가_삭제_API_전환() {
//        Map<String, String> params = new HashMap<>();
//        params.put("name", "브라운");
//        params.put("date", "2023-08-05");
//        params.put("time", "10:00");
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/reservations")
//                .then().log().all()
//                .statusCode(200);
//
//        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
//        assertThat(count).isEqualTo(1);
//
//        RestAssured.given().log().all()
//                .when().delete("/reservations/1")
//                .then().log().all()
//                .statusCode(200);
//
//        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
//        assertThat(countAfterDelete).isEqualTo(0);
//    }
}
