package roomescape.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;

@Sql("/reservation-time-api-test-data.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTimeApiTest {

    @LocalServerPort
    int port;

    @Test
    void 예약_시간_추가() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.parse("11:00"));

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/times/1")
                .body("id", equalTo(1))
                .body("startAt", equalTo(LocalTime.parse("11:00").toString()));
    }

    @Test
    void 예약_시간_단일_조회() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.parse("11:00"));

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times");

        given().log().all()
                .port(port)
                .when().get("/times/1")
                .then().log().all()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("startAt", equalTo(LocalTime.parse("11:00").toString())
                );
    }

    @Test
    void 예약_시간_전체_조회() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.parse("11:00"));

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times");

        given().log().all()
                .port(port)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Sql("/reservation-time-service-test-data.sql")
    @Test
    void 예약_가능한_시간_조회() {

        String targetDay = LocalDate.now().plusDays(1).toString();
        given().log().all()
                .port(port)
                .when().get("/times/available?date=" + targetDay + "&themeId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
    @Test
    void 예약_시간_삭제() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.parse("11:00"));

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times");

        given().log().all()
                .port(port)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }
}
