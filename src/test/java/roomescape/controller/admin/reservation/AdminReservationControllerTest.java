package roomescape.controller.admin.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.reservation.ReservationRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;


@Sql(value = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminReservationControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUpEach() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("예약을 추가 하면 201과 예약 정보를 응답 한다.")
    void addReservation201AndReservation() {
        final String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        final ReservationRequest request = new ReservationRequest(tomorrow, 1L, 1L, 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", containsString("/reservations/"))
                .body("date", is(request.date()))
                .body("time.startAt", is("08:00"))
                .body("theme.name", is("젠틀 먼데이"))
                .body("member.name", is("새양"));
    }
}