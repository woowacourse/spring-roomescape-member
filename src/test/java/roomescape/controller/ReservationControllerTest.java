package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationDao reservationDao;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @DisplayName("모든 예약을 조회한다.")
    @Test
    void getAll() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "커비",
                "2099-12-31", 1, 1);

        // when
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        // then
        assertAll(
                () -> assertThat(reservations.get(0).id()).isEqualTo(1),
                () -> assertThat(reservations.get(0).name()).isEqualTo("커비"),
                () -> assertThat(reservations.get(0).date()).isEqualTo(LocalDate.of(2099, 12, 31)),
                () -> assertThat(reservations.get(0).time().getId()).isEqualTo(1),
                () -> assertThat(reservations.get(0).theme().getId()).isEqualTo(1)
        );

    }

    @DisplayName("예약을 생성한다.")
    @Test
    void create() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마이름", "설명", "썸네일");
        Map<String, String> params = Map.of(
                "name", "커비",
                "date", "2099-12-31",
                "timeId", "1",
                "themeId", "1"
        );

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/1");

        Reservation reservation = reservationDao.findById(1);

        // then
        assertAll(
                () -> assertThat(reservation.getId()).isEqualTo(1),
                () -> assertThat(reservation.getName()).isEqualTo("커비"),
                () -> assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2099, 12, 31)),
                () -> assertThat(reservation.getThemeId()).isEqualTo(1),
                () -> assertThat(reservation.getThemeId()).isEqualTo(1)
        );
    }

    @DisplayName("해당 id의 예약을 삭제한다.")
    @Test
    void delete() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        // when
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        List<Reservation> reservations = reservationDao.findAll();

        // then
        assertThat(reservations).isEmpty();
    }
}
