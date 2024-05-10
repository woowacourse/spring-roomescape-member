package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationTimeTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    void insert() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 5, 8, 12, 30);
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES(?, ?, ?)", "hi", "happy", "abcd.html");
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)", "space", "abcd@gmail.com", "2580", "USER");
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id, created_at) VALUES(?, ?, ?, ?, ?)",
                1L, LocalDate.of(2999, 12, 12), 1, 1, createdAt);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("올바르지 않은 시간 형식으로 입력시 예외처리")
    @Test
    void invalidTimeFormat() {
        Map<String, String> reservationTimeRequest = new HashMap<>();
        reservationTimeRequest.put("startAt", "aaa11");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("올바르지 않은 시간 형식으로 입력시 예외처리")
    @Test
    void deleteTimeInUse() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("테마와 날짜 정보를 주면 시간별 예약가능 여부를 반환한다.")
    @Test
    void availableTime() {
        RestAssured.given().log().all()
                .when().get("/times/available?themeId=1&date=2999-12-12")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
