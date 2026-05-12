package roomescape.acceptance_test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.controller.dto.ReservationCreateRequest;
import roomescape.reservationtime.controller.dto.ReservationTimeCreateRequest;
import roomescape.theme.controller.dto.ThemeCreateRequest;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("예약 생성 후 관리자 페이지에서 예약 목록을 조회한다.")
    public void scenario1() throws JsonProcessingException {
        ReservationCreateRequest reservationRequest = createScenario1Fixture();
        Integer reservationId = createReservation(reservationRequest);

        given().log().all()
                .when()
                .get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.id", hasItem(reservationId))
                .body("reservations.guestName", hasItem(reservationRequest.guestName()))
                .body("reservations.date", hasItem(reservationRequest.date().toString()))
                .body("reservations.time.id", hasItem(reservationRequest.timeId().intValue()))
                .body("reservations.theme.id", hasItem(reservationRequest.themeId().intValue()));
    }

    private ReservationCreateRequest createScenario1Fixture() throws JsonProcessingException {
        Integer reservationTimeId = createReservationTime(
                new ReservationTimeCreateRequest(LocalTime.of(10, 30)));
        Integer themeId = createTheme(
                new ThemeCreateRequest("테마1", "설명", "섬네일"));
        return new ReservationCreateRequest(
                "brown",
                LocalDate.of(2026, 10, 14),
                reservationTimeId.longValue(),
                themeId.longValue());
    }

    @Test
    @DisplayName("예약 삭제 후 관리자 예약 목록에서 사라진다.")
    public void scenario2() throws JsonProcessingException {
        Integer reservationId = createScenario2Fixture();

        given().log().all()
                .pathParam("id", reservationId)
                .when()
                .delete("/admin/reservations/{id}")
                .then().log().all()
                .statusCode(204);

        given().log().all()
                .when()
                .get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.id", not(hasItem(reservationId)));
    }

    private Integer createScenario2Fixture() throws JsonProcessingException {
        LocalTime startAt = LocalTime.of(10, 30);
        ReservationTimeCreateRequest timeRequest = new ReservationTimeCreateRequest(startAt);
        Integer reservationTimeId = createReservationTime(timeRequest);

        ThemeCreateRequest themeRequest = new ThemeCreateRequest("테마1", "설명", "섬네일");
        Integer themeId = createTheme(themeRequest);

        ReservationCreateRequest reservationRequest = new ReservationCreateRequest(
                "brown",
                LocalDate.of(2026, 10, 14),
                reservationTimeId.longValue(),
                themeId.longValue());

        Integer reservationId = createReservation(reservationRequest);
        return reservationId;
    }

    @Test
    @DisplayName("특정 사용자의 이름을 입력해 예약을 조회한다.")
    public void scenario3() throws JsonProcessingException {
        String guestName = "brown";
        createScenario3Fixture(guestName);

        given().log().all()
                .queryParam("guestName", guestName)
                .when()
                .get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.guestName", hasItem(guestName));
    }

    private void createScenario3Fixture(String guestName) throws JsonProcessingException {
        LocalTime startAt = LocalTime.of(10, 30);
        ReservationTimeCreateRequest timeRequest = new ReservationTimeCreateRequest(startAt);
        Integer reservationTimeId = createReservationTime(timeRequest);

        ThemeCreateRequest themeRequest = new ThemeCreateRequest("테마1", "설명", "섬네일");
        Integer themeId = createTheme(themeRequest);

        ReservationCreateRequest reservationRequest = new ReservationCreateRequest(
                guestName,
                LocalDate.of(2026, 10, 14),
                reservationTimeId.longValue(),
                themeId.longValue());

        createReservation(reservationRequest);
    }

    private Integer createReservationTime(ReservationTimeCreateRequest request) throws JsonProcessingException {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .body("startAt", equalTo(request.startAt().toString()))
                .extract().path("id");
    }

    private Integer createTheme(ThemeCreateRequest request) throws JsonProcessingException {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("name", equalTo(request.name()))
                .body("description", equalTo(request.description()))
                .body("thumbnail", equalTo(request.thumbnail()))
                .extract().path("id");
    }

    private Integer createReservation(
            ReservationCreateRequest request
    ) throws JsonProcessingException {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("guestName", equalTo(request.guestName()))
                .body("date", equalTo(request.date().toString()))
                .body("time.id", equalTo(request.timeId().intValue()))
                .body("theme.id", equalTo(request.themeId().intValue()))
                .extract().path("id");
    }

}
