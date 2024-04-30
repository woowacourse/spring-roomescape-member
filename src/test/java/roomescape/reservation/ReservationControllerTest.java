package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.reservation.controller.ReservationController;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.request.ReservationRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ReservationController reservationController;

    @DisplayName("저장된 모든 예약을 조회하고 상태코드 200을 응답한다.")
    @Test
    void findAll() {
        long timeId = insertReservationTimeAndGetId("10:00");
        insertReservation("브라운", "2023-08-05", timeId);
        insertReservation("구구", "2023-08-06", timeId);
        assertReservationCountIsEqualTo(2);

        List<Reservation> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        Integer count = reservationDao.findAll().size();
        assertThat(reservations.size()).isEqualTo(count);
    }

    @DisplayName("예약을 추가하고 상태코드 201을 응답한다.")
    @Test
    void create() {
        long timeId = insertReservationTimeAndGetId("10:00");
        insertReservation("브라운", "2023-08-05", timeId);
        assertReservationCountIsEqualTo(1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest("구구", "2023-08-06", timeId))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        assertReservationCountIsEqualTo(2);
    }

    @DisplayName("저장된 예약을 삭제하고 상태코드 204을 응답한다.")
    @Test
    void delete() {
        long timeId = insertReservationTimeAndGetId("10:00");
        long id = insertReservationAndGetId("브라운", "2023-08-05", timeId);

        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);

        assertReservationCountIsEqualTo(0);
    }

    @DisplayName("컨트롤러와 DB 관련 로직 클래스의 분리를 확인한다.")
    @Test
    void layeredStructure() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    ReservationRequest reservationRequest(String name, String date, long timeId) {
        return new ReservationRequest(name, LocalDate.parse(date), timeId);
    }

    long insertReservationTimeAndGetId(String time) {
        return reservationTimeDao.save(new ReservationTime(0, LocalTime.parse(time))).id();
    }

    void insertReservation(String name, String date, long timeId) {
        insertReservationAndGetId(name, date, timeId);
    }

    long insertReservationAndGetId(String name, String date, long timeId) {
        ReservationTime time = reservationTimeDao.findById(timeId);
        return reservationDao.save(new Reservation(null, name, LocalDate.parse(date), time)).id();
    }

    void assertReservationCountIsEqualTo(int count) {
        assertThat(count).isEqualTo(reservationDao.findAll().size());
    }
}
