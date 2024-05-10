package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.request.ReservationRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.MemberFixture.memberFixture;
import static roomescape.fixture.ReservationTimeFixture.reservationTimeFixture;
import static roomescape.fixture.ThemeFixture.themeFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationTest {

    private final LocalDate reservationDate = LocalDate.of(2030, 12, 12);
    private final LocalDateTime createdAt = LocalDateTime.of(2024, 5, 8, 12, 30);
    private final ReservationRequest reservationRequest = new ReservationRequest(reservationDate, reservationTimeFixture.getId(), themeFixture.getId());

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    void insert() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", reservationTimeFixture.getStartAt());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES(?, ?, ?)", themeFixture.getName(), themeFixture.getDescription(), themeFixture.getDescription());
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)", memberFixture.getName(), memberFixture.getEmail(), memberFixture.getPassword(), memberFixture.getRole().toString());
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id, created_at) VALUES(?, ?, ?, ?, ?)",
                memberFixture.getId(), reservationDate, reservationTimeFixture.getId(), themeFixture.getId(), createdAt);
    }

    String getToken() {
        Map<String, String> params = new HashMap<>();
        params.put("email", memberFixture.getEmail());
        params.put("password", memberFixture.getPassword());

        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie");
        return token.substring("token=".length(), token.indexOf(';'));
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("예약 추가 API 테스트")
    @Test
    void createReservation() {
        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("date", "2025-12-12");
        reservationRequest.put("timeId", "1");
        reservationRequest.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getToken())
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2))
                .body("date", is("2025-12-12"));
    }

    @DisplayName("예약 조회 API 테스트")
    @Test
    void getReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("예약 취소 API 테스트")
    @Test
    void deleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @DisplayName("로그인하지 않고 예약 요청시 예외처리")
    @Test
    void notLogIn() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("올바르지 않은 날짜 형식으로 입력시 예외처리")
    @Test
    void invalidDateFormat() {
        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("date", "2025-aa-bb");
        reservationRequest.put("timeId", "1");
        reservationRequest.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getToken())
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("올바르지 않은 예약자명 형식으로 입력시 예외처리")
    @Test
    void invalidTimeIdFormat() {
        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("date", "2030-12-12");
        reservationRequest.put("timeId", "a");
        reservationRequest.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getToken())
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("지나간 시점에 대한 예약시 예외처리")
    @Test
    void pastTimeSlotReservation() {
        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("date", "1999-12-12");
        reservationRequest.put("timeId", "1");
        reservationRequest.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getToken())
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("동일한 날짜와 시간에 중복 예약시 예외 처리")
    @Test
    void duplicateReservation() {
        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("date", "2030-12-12");
        reservationRequest.put("timeId", "1");
        reservationRequest.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getToken())
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
