package roomescape.controller;

import static roomescape.TestFixture.DATE;
import static roomescape.TestFixture.RESERVATION_TIME_10AM;
import static roomescape.TestFixture.ROOM_THEME1;
import static roomescape.TestFixture.TIME;
import static roomescape.TestFixture.VALID_STRING_DATE;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.service.dto.request.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {
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

    @DisplayName("모든 예약 내역 조회")
    @Test
    void findAllReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("예약 추가 테스트")
    @Test
    void createReservation() {
        // given
        Map reservationRequest = createReservationRequest("브라운",
                VALID_STRING_DATE);
        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("사용자 이름에 빈문자열 입력시 400을 응답한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void invalidNameReservation(String value) {
        // given
        Map reservationRequest = createReservationRequest(value,
                VALID_STRING_DATE);
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("날짜 양식을 잘못 입력할 시 400을 응답한다.")
    @ParameterizedTest
    @ValueSource(strings = {"20223-10-11", "2024-13-1"})
    void invalidDateReservation(String value) {
        // given
        Map reservationRequest = createReservationRequest("브라운", value);
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지나간 시간 예약 시도 시 400을 응답한다.")
    @Test
    void outdatedReservation() {
        // given
        Map reservationRequest = createReservationRequest("브라운", "2023-12-12");
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("중복된 시간 예약 시도 시 400을 응답한다.")
    @Test
    void duplicateReservation() {
        // given
        Map reservationRequest = createReservationRequest("브라운",
                VALID_STRING_DATE);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all();
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("참조키가 존재하지 않음으로 인한 예약 추가 실패 테스트")
    @Test
    void noPrimaryKeyReservation() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest("브라운", DATE, 1L, 1L))
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("예약 취소 성공 테스트")
    @Test
    void deleteReservationSuccess() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(
                new ReservationTime(TIME));
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME1);
        Reservation savedReservation = reservationDao.save(
                new Reservation("브라운", DATE, savedReservationTime,
                        savedRoomTheme));
        // when & then
        Long id = savedReservation.getId();
        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all().assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("예약 취소 실패 테스트")
    @Test
    void deleteReservationFail() {
        // given
        long invalidId = 0;
        // when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + invalidId)
                .then().log().all().assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }

    private Map createReservationRequest(String name, String date) {
        ReservationTime savedReservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME1);

        return Map.of(
                "name", name,
                "date", date,
                "timeId", savedReservationTime.getId(),
                "themeId", savedRoomTheme.getId());
    }
}
