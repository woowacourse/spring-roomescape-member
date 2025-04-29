package roomescape;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import roomescape.reservation.domain.dto.ReservationReqDto;
import roomescape.reservation.domain.dto.ReservationResDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ExtendWith({SpringExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void connection() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAll() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:40");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "브라운", "2023-08-05", "1");

        List<ReservationResDto> resDtos = RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract()
            .jsonPath().getList(".", ReservationResDto.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(resDtos.size()).isEqualTo(count);
    }

    @Test
    void add() {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        LocalTime dummyTime = localDateTime.toLocalTime();
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", dummyTime.toString());

        String dummyName = "브라운";
        LocalDate dummyDate = localDateTime.toLocalDate();

        ReservationReqDto dto = new ReservationReqDto(dummyName, dummyDate, 1L);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(dto)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void delete() {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        LocalTime dummyTime = localDateTime.toLocalTime();
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", dummyTime.toString());

        String dummyName = "브라운";
        LocalDate dummyDate = localDateTime.toLocalDate();

        ReservationReqDto dto = new ReservationReqDto(dummyName, dummyDate, 1L);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(dto)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
            .when().delete("/reservations/1")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }
}
