package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.response.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setupDatabase() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "12:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:40");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
            "탈출 스페셜", "탈출하는 내용", "abc.jpg");
    }

    @Test
    void 사단계() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null)
                .next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 오단계() {
        jdbcTemplate.update(
            "INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES (?, ?, ?, ?)",
            LocalDate.now().plusDays(1),
            1L,
            1L,
            1L
        );

        List<ReservationResponse> reservations = RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200).extract()
            .jsonPath().getList(".", ReservationResponse.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation",
            Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    void 칠단계() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/times")
            .then().log().all()
            .statusCode(201);

        RestAssured.given().log().all()
            .when().delete("/times/1")
            .then().log().all()
            .statusCode(204);
    }
}
