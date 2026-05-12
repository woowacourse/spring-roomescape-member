package roomescape.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.web.support.DatabaseHelper;
import roomescape.web.support.SpringWebTest;

@SpringWebTest
public class ExceptionTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DatabaseHelper databaseHelper;

    @BeforeEach
    void setup() {
        databaseHelper.clear();
    }

    @DisplayName("예약 날짜가 오늘 (5월 1일)보다 이전이면 예외가 발생한다.")
    @Test
    void makeReservation_invalid_date() {
        //given
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(LocalTime.of(10, 0))
        );

        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                "테마", "설명", "thumbnailUrl"
        );

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-04-30");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422);
    }

    @DisplayName("예약 시간이 오늘(5월 1일), 이 시간(09:00) 이전이면 예외가 발생한다.")
    @Test
    void makeReservation_invalid_time() {
        //given
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(LocalTime.of(8, 0))
        );

        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                "테마", "설명", "thumbnailUrl"
        );

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-05-01");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422);
    }

    @DisplayName("기존에 예약이 있으면 예외가 발생한다.")
    @Test
    void makeReservation_duplicate_reservation() {
        //given
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(LocalTime.of(10, 0))
        );

        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                "테마", "설명", "thumbnailUrl"
        );

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-05-01");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @DisplayName("예약에 사용 중인 예외를 삭제하면 예외가 발생한다.")
    @Test
    void delete_time_in_use() {
        //given
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(LocalTime.of(10, 0))
        );

        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                "테마", "설명", "thumbnailUrl"
        );

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-05-01");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(409);
    }

    @DisplayName("예약 시, name에 null이나 공백, 빈 문자열이 들어오면 예외가 발생한다.")
    @Test
    void  makeReservation_invalid_name_form() {
        //given
        Map<String, Object> paramsWithNull = new HashMap<>();
        paramsWithNull.put("name", null);
        paramsWithNull.put("date", "2026-04-29");
        paramsWithNull.put("timeId", 1L);
        paramsWithNull.put("themeId", 1L);

        Map<String, Object> paramsWithEmpty = new HashMap<>();
        paramsWithEmpty.put("name", "");
        paramsWithEmpty.put("date", "2026-04-29");
        paramsWithEmpty.put("timeId", 1L);
        paramsWithEmpty.put("themeId", 1L);

        Map<String, Object> paramsWithWhiteSpace = new HashMap<>();
        paramsWithWhiteSpace.put("name", " ");
        paramsWithWhiteSpace.put("date", "2026-04-29");
        paramsWithWhiteSpace.put("timeId", 1L);
        paramsWithWhiteSpace.put("themeId", 1L);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithNull)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithEmpty)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithWhiteSpace)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 시, date에 null이나 날짜 형식 아닌 값이 들어오면 예외가 발생한다.")
    @Test
    void makeReservation_invalid_date_form() {
        //given
        Map<String, Object> paramsWithoutDate = new HashMap<>();
        paramsWithoutDate.put("name", "브라운");
        paramsWithoutDate.put("date", null);
        paramsWithoutDate.put("timeId", 1L);
        paramsWithoutDate.put("themeId", 1L);

        Map<String, Object> paramsWithIllegalDateForm = new HashMap<>();
        paramsWithIllegalDateForm.put("name", "브라운");
        paramsWithIllegalDateForm.put("date", "illegal_form");
        paramsWithIllegalDateForm.put("timeId", 1L);
        paramsWithIllegalDateForm.put("themeId", 1L);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithoutDate)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithIllegalDateForm)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 시, timeId, themeId 중 하나라도 null이면 예외가 발생한다.")
    @Test
    void makeReservation_invalid_timeId_And_themeId_form() {
        //given
        Map<String, Object> paramsWithoutTimeId = new HashMap<>();
        paramsWithoutTimeId.put("name", "브라운");
        paramsWithoutTimeId.put("date", "2026-04-29");
        paramsWithoutTimeId.put("timeId", null);
        paramsWithoutTimeId.put("themeId", 1L);

        Map<String, Object> paramsWithoutThemeId = new HashMap<>();
        paramsWithoutThemeId.put("name", "브라운");
        paramsWithoutThemeId.put("date", "2026-04-29");
        paramsWithoutThemeId.put("timeId", 1L);
        paramsWithoutThemeId.put("themeId", null);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithoutTimeId)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithoutThemeId)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("시간 등록 시, startAt에 null이나 시간 형식 아닌 값이 들어오면 예외가 발생한다.")
    @Test
    void createTimes_invalid_time_form() {
        //given
        Map<String, Object> paramsWithoutStartAt = new HashMap<>();
        paramsWithoutStartAt.put("startAt", null);

        Map<String, Object> paramsWithIllegalStartAt = new HashMap<>();
        paramsWithIllegalStartAt.put("startAt", "illegal_format");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithoutStartAt)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithIllegalStartAt)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("테마 등록 시, name에 null이나 공백, 빈 문자열이 들어오면 예외가 발생한다.")
    @Test
    void createTheme_invalid_name_form() {
        //given
        Map<String, Object> paramsWithoutName = new HashMap<>();
        paramsWithoutName.put("name", null);
        paramsWithoutName.put("description", "설명");
        paramsWithoutName.put("thumbnailUrl", "thumbnailUrl");

        Map<String, Object> paramsWithEmptyName = new HashMap<>();
        paramsWithoutName.put("name", "");
        paramsWithoutName.put("description", "설명");
        paramsWithoutName.put("thumbnailUrl", "thumbnailUrl");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithoutName)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithEmptyName)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);
    }


    @DisplayName("테마 등록 시, description, thumbnailUrl 중 하나라도 null이면 예외가 발생한다.")
    @Test
    void createTheme_invalid_description_and_thumbnailUrl_form() {
        //given
        Map<String, Object> paramsWithoutDescription = new HashMap<>();
        paramsWithoutDescription.put("name", "테마");
        paramsWithoutDescription.put("description", null);
        paramsWithoutDescription.put("thumbnailUrl", "thumbnailUrl");

        Map<String, Object> paramsWithoutThumbnailUrl = new HashMap<>();
        paramsWithoutDescription.put("name", "테마");
        paramsWithoutDescription.put("description", "설명");
        paramsWithoutDescription.put("thumbnailUrl", null);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithoutDescription)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(paramsWithoutThumbnailUrl)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);
    }
}
