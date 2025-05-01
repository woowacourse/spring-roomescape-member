package roomescape.reservationtime;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservationtime.dto.ReservationTimeRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationTimeApiTest {

    private final JdbcTemplate jdbcTemplate;
    private final int port;

    public ReservationTimeApiTest(
            @LocalServerPort final int port,
            @Autowired final JdbcTemplate jdbcTemplate
    ){
        this.port = port;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("시간 생성")
    @Test
    void createTime() {
        // given & when & then
        RestAssured.given().port(port).log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeRequest(LocalTime.of(10, 0)))
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("시간 모두 조회")
    @Test
    void findAllTime() {
        // given & when & then
        RestAssured.given().port(port).log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @DisplayName("시간 삭제")
    @Test
    void deleteTime() {
        // given
        final ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        givenCreateTime(request);

        // when & then
        RestAssured.given().port(port).log().all()
                .when().delete("times/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 아이디를 가진 시간을 삭제하려고하면 404를 반환한다.")
    @Test
    void deleteTime1() {
        // given & when & then
        RestAssured.given().port(port).log().all()
                .when().delete("times/100")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("startAt 관련 api 테스트")
    @Test
    void 칠단계() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        // when & then
        RestAssured.given().port(port).log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().port(port).log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().port(port).log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    private void givenCreateTime(final ReservationTimeRequest body) {
        RestAssured.given().port(port).log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }


}
