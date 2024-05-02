package roomescape.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static roomescape.TestSetting.createReservationRequest;
import static roomescape.TestSetting.createReservationTime;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.repository.ReservationTimeRepository;

@Sql("/reservation-api-test-data.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationApiTest {
    @LocalServerPort
    int port;

    @Test
    void 예약_추가() {
        ReservationRequest reservationRequest = new ReservationRequest("ted", LocalDate.now().plusDays(1), 1L, 1L);

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/1")
                .body("id", equalTo(1))
                .body("name", equalTo("ted"))
                .body("date", equalTo(LocalDate.now().plusDays(1).toString()))
                .body("time.id", equalTo(1))
                .body("theme.id", equalTo(1)
                );
    }

    @Test
    void 예약_단일_조회() {
        ReservationRequest reservationRequest = new ReservationRequest("ted", LocalDate.now().plusDays(1), 1L, 1L);

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations");

        given().log().all()
                .port(port)
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("ted"))
                .body("date", equalTo(LocalDate.now().plusDays(1).toString()))
                .body("time.id", equalTo(1))
                .body("theme.id", equalTo(1));
    }

    @Test
    void 예약_전체_조회() {
        ReservationRequest reservationRequest = new ReservationRequest("ted", LocalDate.now().plusDays(1), 1L, 1L);

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations");

        given().log().all()
                .port(port)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 예약_삭제() {
        ReservationRequest reservationRequest = new ReservationRequest("ted", LocalDate.now().plusDays(1), 1L, 1L);

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations");

        given().log().all()
                .port(port)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }
}
