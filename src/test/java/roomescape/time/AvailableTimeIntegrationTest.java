package roomescape.time;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.datasource.url=jdbc:h2:mem:availabledb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
)
class AvailableTimeIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_date RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE theme RESTART IDENTITY");

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "12:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "14:00");
        jdbcTemplate.update("INSERT INTO reservation_date (date) VALUES (?)", LocalDate.now().plusDays(1).toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url, is_active) VALUES (?, ?, ?, ?)",
                "공포", "공포 테마", "https://horror.jpg", true);
    }

    @Test
    @DisplayName("예약 가능 시간 조회 → 예약 생성 → 다시 조회 시 해당 시간이 빠진다.")
    void availableTime_flow() {
        List<?> beforeTimes = RestAssured.given().log().all()
                .when().get("/member/times?date=" + LocalDate.now().plusDays(1) + "&themeId=1")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".");

        assertThat(beforeTimes).hasSize(3);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "한다");
        reservation.put("dateId", 1);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/member/reservations")
                .then().log().all()
                .statusCode(201);

        List<?> afterTimes = RestAssured.given().log().all()
                .when().get("/member/times?date=" + LocalDate.now().plusDays(1) + "&themeId=1")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".");

        assertThat(afterTimes).hasSize(2);
    }
}
