package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.EntityRepository;
import roomescape.utils.JdbcTemplateUtils;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ReservationApiTest {

    @Autowired
    private EntityRepository<Reservation> reservationRepository;

    @Autowired
    private EntityRepository<ReservationTime> reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    @DisplayName("어드민 페이지로 접근할 수 있다.")
    @Test
    void test1() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민이 예약 관리 페이지에 접근한다.")
    @Test
    void test2() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("모든 예약 정보를 반환한다.")
    @Test
    void test3() {
        // given
        LocalTime time = LocalTime.of(15, 0);
        ReservationTime reservationTime = ReservationTime.withoutId(time);
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        Reservation reservation = Reservation.withoutId("꾹", LocalDate.now(), savedReservationTime);
        reservationRepository.save(reservation);

        // then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("예약 정보를 추가한다.")
    @Test
    void test4() {
        // given
        ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", savedReservationTime.getId());

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("존재하지 않는 예약 시간 ID 를 추가하면 예외를 반환한다.")
    @Test
    void test5() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void test6() {
        // given
        String name = "브라운";
        LocalDate now = LocalDate.now();

        ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        Reservation reservation = Reservation.withoutId(name, now, savedReservationTime);
        Reservation saved = reservationRepository.save(reservation);
        Long id = saved.getId();

        // when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + id.intValue())
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 예약을 삭제할 경우 NOT_FOUND 반환")
    @Test
    void test7() {
        RestAssured.given().log().all()
                .when().delete("/reservations/4")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("시간 관리 페이지에 접근한다.")
    @Test
    void test8() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(200);
    }
}
