package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.TimeRepository;
import roomescape.presentation.controller.ReservationController;
import roomescape.presentation.dto.response.ReservationResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MissionStepTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TimeRepository timeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("/ 요청 시 admin/reservation으로 리디렉션")
    @Test
    void welcomePage_redirect_to_reservationPage() {
        RestAssured.given().log().all()
                .redirects().follow(false) // 리디렉션 따라가지 말고 그대로 응답 확인
                .when().get("/")
                .then().log().all()
                .statusCode(302) // 또는 301, 실제 리디렉션 코드에 따라 다름
                .header("Location", endsWith("/admin/reservation"));
    }

    @DisplayName("/admin 요청 시 200 OK 응답")
    @Test
    void request_adminPage_then_200() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("1단계 - /admin/reservation 요청 시 200 OK")
    @Test
    void request_ReservationAdminPage_then_200() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("2단계 - 모든 예약을 가져오는 api 호출 시, 현재 저장소의 예약 개수와 일치해야한다.")
    @Test
    void request_getAllReservations() {
        int reservationsCount = reservationRepository.findAll().size();

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(reservationsCount));
    }

    @DisplayName("3단계 - 예약 추가 api 호출 시, id가 정상적으로 부여된다.")
    @Test
    public void request_addReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)",
                1L, "10:00");

        int repositorySize = reservationRepository.findAll().size();
        int expectedSize = repositorySize + 1;

        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        String date = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        params.put("date", date);
        params.put("timeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(expectedSize));

        int afterAddSize = reservationRepository.findAll().size();
        assertThat(afterAddSize).isEqualTo(expectedSize);
    }

    @DisplayName("3단계 - id로 예약을 삭제할 수 있다.")
    @Test
    void requestDeleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 사단계() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 오단계() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)",
                1L, "10:00");

        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)",
                "브라운", "2023-08-05", 1L
        );

        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    void 육단계() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)",
                1L, "10:00");

        int beforeCount = reservationRepository.findAll().size();

        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        String date = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        params.put("date", date);
        params.put("timeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(beforeCount + 1);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(beforeCount);
    }

    @Test
    void 칠단계() {
        int beforeSize = timeRepository.findAll().size();

        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(beforeSize + 1));

        RestAssured.given().log().all()
                .when().delete(String.format("/times/%d", beforeSize + 1))
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 팔단계() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)",
                1L, "10:00");

        int beforeSize = reservationRepository.findAll().size();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        String date = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        reservation.put("date", date);
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(beforeSize + 1));
    }

    @Autowired
    private ReservationController reservationController;

    @Test
    void 구단계() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    @DisplayName("예약이 존재하는 시간은 삭제 불가")
    @Test
    void cannotDeleteTime_when_hasReservation() {
        Long timeId = 1L;
        ReservationTime reservationTime = ReservationTime.of(timeId, LocalTime.of(10, 0));
        timeRepository.save(reservationTime);
        reservationRepository.save(Reservation.of(1L, "testName", LocalDate.of(2025,1,1), reservationTime));

        RestAssured.given().log().all()
                .when().delete(String.format("/times/%d", timeId))
                .then().log().all()
                .statusCode(405);
    }

    @DisplayName("존재하지 않는 id로 조회 시 예외 발생")
    @Test
    void error_when_id_notFound() {
        Long timeId = 999L;
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        String date = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        reservation.put("date", date);
        reservation.put("timeId", timeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("중복된 일시로 예약 생성 시 예외 발생")
    @Test
    void error_when_duplicateReservation() {
        // given
        Long timeId = 1L;
        ReservationTime reservationTime = ReservationTime.of(timeId, LocalTime.of(10, 0));
        timeRepository.save(reservationTime);
        LocalDate date = LocalDate.now().plusDays(1);
        reservationRepository.save(Reservation.of(1L, "testName", date, reservationTime));

        // when
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", date);
        reservation.put("timeId", timeId);

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
