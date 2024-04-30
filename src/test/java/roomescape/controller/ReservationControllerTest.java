package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.service.ReservationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        List<Reservation> reservations = reservationDao.findAll();
        for (Reservation reservation : reservations) {
            reservationDao.deleteById(reservation.getId());
        }
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        for (ReservationTime reservationTime : reservationTimes) {
            reservationTimeDao.deleteById(reservationTime.getId());
        }
    }

    @DisplayName("모든 예약 내역 조회 테스트")
    @Test
    void findAllReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("예약 추가 테스트")
    @Test
    void createReservation() {
        //given
        reservationTimeDao.save(new ReservationTime("10:00"));
        //then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest("브라운", "2023-08-05", 1L))
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("참조키가 존재하지 않음으로 인한 예약 추가 실패 테스트")
    @Test
    void createReservationFail() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest("브라운", "2023-08-05", 1L))
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예약 취소 성공 테스트")
    @Test
    void deleteReservationSuccess() {
        //given
        ReservationTime reservationTime = reservationTimeDao.save(new ReservationTime("10:00"));
        Reservation reservation = reservationDao.save(
                new Reservation("brown", "2024-11-15", reservationTime));
        Long id = reservation.getId();
        //then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all().assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("예약 취소 실패 테스트")
    @Test
    void deleteReservationFail() {
        //given
        long invalidId = 0;
        //then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + invalidId)
                .then().log().all().assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
