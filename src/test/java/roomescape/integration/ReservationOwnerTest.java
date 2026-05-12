package roomescape.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.integration.support.DatabaseHelper;
import roomescape.integration.support.SpringWebTest;

@SpringWebTest
public class ReservationOwnerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DatabaseHelper databaseHelper;

    @BeforeEach
    void setup() {
        databaseHelper.clear();
    }

    @DisplayName("이름을 header로 넘겨 자신의 예약을 삭제한다.")
    @Test
    void deleteMyReservationById_success() {
        //given
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(LocalTime.of(10, 0))
        );

        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                "테마", "설명", "thumbnailUrl"
        );

        jdbcTemplate.update(
                "INSERT INTO reservation (name, reservation_date, time_id,  theme_id) VALUES (?, ?, ?, ?)",
                "brown", Date.valueOf(LocalDate.of(2026, 5, 5)), 1L, 1L
        );

        //when & then
        RestAssured.given().log().all()
                .header("Authorization", "brown")
                .contentType(ContentType.JSON)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

    }

    @DisplayName("예약 삭제 시, id에 해당하는 예약이 없으면 예외가 발생한다.")
    @Test
    void deleteMyReservationById_id_x() {
        //given
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(LocalTime.of(10, 0))
        );

        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                "테마", "설명", "thumbnailUrl"
        );

        //when & then
        RestAssured.given().log().all()
                .header("Authorization", "brown")
                .contentType(ContentType.JSON)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("예약 삭제 시, 자신의 예약이 아니면 예외가 발생한다.")
    @Test
    void deleteMyReservationById__not_owner() {
        //given
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(LocalTime.of(10, 0))
        );

        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                "테마", "설명", "thumbnailUrl"
        );

        jdbcTemplate.update(
                "INSERT INTO reservation (name, reservation_date, time_id,  theme_id) VALUES (?, ?, ?, ?)",
                "brown", Date.valueOf(LocalDate.of(2026, 5, 5)), 1L, 1L
                );


        //when & then
        RestAssured.given().log().all()
                .header("Authorization", "pobi")
                .contentType(ContentType.JSON)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(403);
    }
}
