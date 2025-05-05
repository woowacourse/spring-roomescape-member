package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.response.ReservationResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final LocalDate tomorrow = LocalDate.now().plusDays(1);

    @Test
    @DisplayName("예약 조회 시 저장된 예약 내역을 모두 가져온다")
    void test1() {
        // given
        insertNewReservationWithJdbcTemplate(1L, 1L);

        // when
        List<ReservationResponseDto> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponseDto.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        // then
        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("예약 등록 시 잘못된 날짜로 요청하는 경우 400에러를 반환한다.")
    void test2() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "invalidDateRequest");
        params.put("timeId", "1");
        params.put("themeId", "1");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("존재하지 않는 time_id 를 이용해 예약을 등록하고자 하는 경우 404 를 반환한다.")
    void test3() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", tomorrow.toString());
        params.put("timeId", "345");
        params.put("themeId", "1");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("존재하지 않는 themeId 를 이용해 예약을 등록하고자 하는 경우 404 를 반환한다.")
    void test4() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", tomorrow.toString());
        params.put("timeId", "1");
        params.put("themeId", "123");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("이미 예약된 테마와 시간에 또 다른 예약을 등록하고자 하는 경우 409 를 반환한다")
    void test5() {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        insertNewReservationWithJdbcTemplate(timeId, themeId);

        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", tomorrow.toString());
        params.put("timeId", timeId.toString());
        params.put("themeId", themeId.toString());

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("정상적으로 예약이 등록되는 경우 201을 반환한다")
    void test6() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", tomorrow.toString());
        params.put("timeId", "1");
        params.put("themeId", "1");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("특정 예약을 삭제하는 경우 성공 시 204를 반환한다")
    void test7() {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        Long savedId = insertNewReservationWithJdbcTemplate(timeId, themeId);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/reservations/" + savedId)
                .then().log().all()
                .statusCode(204);
    }

    private Long insertNewReservationWithJdbcTemplate(final Long timeId, final Long themeId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", new String[]{"id"});
            ps.setString(1, "히로");
            ps.setDate(2, Date.valueOf(tomorrow));
            ps.setLong(3, timeId);
            ps.setLong(4, themeId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
