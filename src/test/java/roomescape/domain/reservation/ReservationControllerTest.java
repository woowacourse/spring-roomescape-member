package roomescape.domain.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long futureDateId;
    private Long timeId;
    private Long themeId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        KeyHolder dateKeyHolder = new GeneratedKeyHolder();
        String futureDate = LocalDate.now().plusDays(10).toString();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into reservation_date(play_day) values (?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, futureDate);
            return ps;
        }, dateKeyHolder);
        futureDateId = Objects.requireNonNull(dateKeyHolder.getKey()).longValue();

        KeyHolder timeKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into reservation_time(start_at) values (?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "22:00");
            return ps;
        }, timeKeyHolder);
        timeId = Objects.requireNonNull(timeKeyHolder.getKey()).longValue();

        KeyHolder themeKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into theme(name, content, url) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "테스트테마");
            ps.setString(2, "설명");
            ps.setString(3, "url");
            return ps;
        }, themeKeyHolder);
        themeId = Objects.requireNonNull(themeKeyHolder.getKey()).longValue();
    }

    @Test
    @DisplayName("예약을 생성한다.")
    void createReservation() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "테스터");
        params.put("dateId", futureDateId);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(201)
            .body("name", is("테스터"));
    }

    @Test
    @DisplayName("이름으로 예약을 조회한다.")
    void getReservationsByName() {
        jdbcTemplate.update("insert into reservation(name, date_id, time_id, theme_id) values (?, ?, ?, ?)",
            "테스터", futureDateId, timeId, themeId);

        RestAssured.given().log().all()
            .param("name", "테스터")
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200)
            .body("any { it.name == '테스터' }", is(true));
    }

    @Test
    @DisplayName("예약을 취소(삭제)한다.")
    void cancelReservation() {
        KeyHolder reservationKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into reservation(name, date_id, time_id, theme_id) values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "취소테스터");
            ps.setLong(2, futureDateId);
            ps.setLong(3, timeId);
            ps.setLong(4, themeId);
            return ps;
        }, reservationKeyHolder);
        Long reservationId = Objects.requireNonNull(reservationKeyHolder.getKey()).longValue();

        RestAssured.given().log().all()
            .when().delete("/reservations/" + reservationId)
            .then().log().all()
            .statusCode(204);
    }
}
