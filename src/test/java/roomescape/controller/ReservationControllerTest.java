package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.dto.request.ReservationMemberCreateRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String cookie;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "오리와 호랑이", "오리들과 호랑이들 사이에서 살아남기", "https://image.jpg");
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES (?, ?, ?, ?)"
                , "2023-08-05", 1, 1, 1);
        Map<String, String> admin = Map.of(
                "email", "aaa@naver.com",
                "password", "1111"
        );

        cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(admin)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];
    }

    @DisplayName("예약 목록을 읽을 수 있다.")
    @Test
    void readReservations() {
        int size = RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getInt("size()");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(size).isEqualTo(count);
    }

    @DisplayName("예약을 DB에 추가할 수 있다.")
    @Test
    void createReservation() {
        ReservationMemberCreateRequest params = new ReservationMemberCreateRequest
                (LocalDate.of(2040, 8, 5), 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("cookie", cookie)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/2");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(2);
    }

    @DisplayName("삭제할 id를 받아서 DB에서 해당 예약을 삭제 할 수 있다.")
    @Test
    void deleteReservation() {
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }
}
