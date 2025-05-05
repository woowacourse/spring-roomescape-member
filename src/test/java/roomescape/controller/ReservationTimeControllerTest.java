package roomescape.controller;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {

    private static final LocalTime TIME = LocalTime.of(10, 0);
    private static final Long THEME_ID = 1L;
    private static final String DATE = LocalDate.now().toString();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM reservation");
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다")
    void readAllTimes() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("특정 날짜와 테마의 가능한 예약 시간을 조회한다")
    void readAvailableTimes() {
        RestAssured.given().log().all()
                .param("date", DATE)
                .param("themeId", THEME_ID)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 시간을 생성한다")
    void createTime() {
        Map<String, Object> request = createTimeRequest();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2),
                        "startAt", is(TIME.toString()));
    }

    @Test
    @DisplayName("예약 시간을 삭제한다")
    void deleteTime() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    private Map<String, Object> createTimeRequest() {
        Map<String, Object> time = new HashMap<>();
        time.put("startAt", TIME.toString());
        return time;
    }
}

