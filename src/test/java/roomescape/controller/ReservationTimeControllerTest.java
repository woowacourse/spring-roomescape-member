package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.RESERVATION_TIME_FIXTURE;
import static roomescape.TestFixture.ROOM_THEME_FIXTURE;
import static roomescape.TestFixture.TIME_FIXTURE;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.dto.request.ReservationTimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private RoomThemeDao roomThemeDao;

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
        List<RoomTheme> roomThemes = roomThemeDao.findAll();
        for (RoomTheme roomTheme : roomThemes) {
            roomThemeDao.deleteById(roomTheme.getId());
        }
    }

    @DisplayName("모든 예약 시간 조회 테스트")
    @Test
    void findAllReservationTime() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("예약 시간 추가 테스트")
    @Test
    void createReservationTime() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeRequest(TIME_FIXTURE))
                .when().post("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 양식의 시간 입력 시 400을 응답한다.")
    @Test
    void invalidTimeInput() {
        // given
        Map<String, String> reservationTimeRequest = new HashMap<>();
        reservationTimeRequest.put("startAt", "00:61");
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("중복된 시간 추가 시도 시, 추가되지 않고 400을 응답한다.")
    @Test
    void duplicateReservationTime() {
        // given
        createReservationTime();
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeRequest(TIME_FIXTURE))
                .when().post("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예약 시간 삭제 성공 테스트")
    @Test
    void deleteReservationTImeSuccess() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        Long id = reservationTime.getId();
        // when & then
        RestAssured.given().log().all()
                .when().delete("/times/" + id)
                .then().log().all().assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("예약 시간 삭제 실패 테스트")
    @Test
    void deleteReservationTimeFail() {
        // given
        long invalidId = 0;
        // when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + invalidId)
                .then().log().all().assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("예약 시간 삭제 시, 해당 id를 참조하는 예약도 삭제된다.")
    @Test
    void deleteReservationTimeDeletesReservationAlso() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        RoomTheme roomTheme = roomThemeDao.save(ROOM_THEME_FIXTURE);
        reservationDao.save(
                new Reservation(new Name("brown"), DATE_FIXTURE, reservationTime, roomTheme));
        Long timeId = reservationTime.getId();
        // when
        Response deleteResponse = RestAssured.given().log().all()
                .when().delete("/times/" + timeId)
                .then().log().all().extract().response();
        // then
        Response reservationResponse = RestAssured.given().log().all()
                .when().get("reservations")
                .then().log().all().extract().response();
        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(
                        HttpStatus.NO_CONTENT.value()),
                () -> assertThat(reservationResponse.jsonPath().getList(".")).isEmpty()
        );
    }
}
