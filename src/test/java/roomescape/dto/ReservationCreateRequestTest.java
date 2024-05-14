package roomescape.dto;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationCreateRequestTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("날짜가 비어있을 경우 BAD REQUEST를 반환한다.")
    @Test
    void create_nullDate_badRequest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        Map<String, String> params = Map.of(
                "name", "테니",
                "date", "",
                "timeId", "1",
                "themeId", "1"
        );

        // when && then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @DisplayName("예약 시간 id가 비어있을 경우 BAD REQUEST를 반환한다.")
    @Test
    void create_nullTimeId_badRequest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        Map<String, String> params = Map.of(
                "name", "테니",
                "date", "2024-08-30",
                "timeId", "",
                "themeId", "1"
        );

        // when && then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @DisplayName("테마 id가 비어있을 경우 BAD REQUEST를 반환한다.")
    @Test
    void create_nullThemeId_badRequest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        Map<String, String> params = Map.of(
                "name", "테니",
                "date", "2024-08-30",
                "timeId", "1",
                "themeId", ""
        );

        // when && then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
