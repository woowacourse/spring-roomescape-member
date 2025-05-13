package roomescape.integration.admin;

import static roomescape.test.fixture.DateFixture.NEXT_DAY;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.MemberRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("예약 가능 시간을 추가할 수 있다")
    @Test
    void canAddReservationTime() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("startAt", LocalTime.of(10, 0).toString());

        // when & then
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("location", "/times/1");
    }

    @DisplayName("예약 가능 시간을 추가할 때, 이미 존재하는 예약 가능 시간은 추가할 수 없다")
    @Test
    void cannotCreateReservationTimeWhenExist() {
        // given
        LocalTime time = LocalTime.now();
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", time.toString());

        Map<String, String> params = new HashMap<>();
        params.put("startAt", time.toString());

        // when & then
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 가능 시간을 삭제할 수 있다")
    @Test
    void canDeleteReservationTime() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now().toString());

        // when & then
        RestAssured
                .given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("예약을 삭제할 때, 이미 해당 시간에 대한 예약 데이터가 존재한다면 삭제가 불가능하다")
    @Test
    void cannotDeleteReservationTimeWhenExistReservation() {
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.now().toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1, 1);

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }
}
