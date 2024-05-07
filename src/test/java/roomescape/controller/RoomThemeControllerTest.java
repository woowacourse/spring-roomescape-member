package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.RESERVATION_TIME_FIXTURE;
import static roomescape.TestFixture.ROOM_THEME_FIXTURE;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
import roomescape.dto.request.RoomThemeCreateRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomThemeControllerTest {
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

    @DisplayName("테마 전체 조회 테스트")
    @Test
    void findAllThemes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("테마 추가 테스트")
    @Test
    void createTheme() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new RoomThemeCreateRequest("레벨2 탈출", "우테코 레벨2",
                        "https://i.pinimg.com/236x/6e"))
                .when().post("/themes")
                .then().log().all().assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("비어있거나 null인 이름이 존재하는 경우 400을 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeWithNullOrEmptyName(String value) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new RoomThemeCreateRequest(value, "desc", "th.jpg"))
                .when().post("/themes")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("비어있거나 null인 설명 존재하는 경우 400을 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeWithNullOrEmptyDescription(String value) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new RoomThemeCreateRequest("name", value, "th.jpg"))
                .when().post("/themes")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("비어있거나 null인 이름이 존재하는 경우 400을 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeWithNullOrEmptyThumbnail(String value) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new RoomThemeCreateRequest("name", "desc", value))
                .when().post("/themes")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테마 삭제 성공 테스트")
    @Test
    void deleteTheme() {
        // given
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME_FIXTURE);
        // when & then
        RestAssured.given().log().all()
                .when().delete("/themes/" + savedRoomTheme.getId())
                .then().log().all().assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("예약 시간 삭제 실패 테스트")
    @Test
    void deleteReservationTimeFail() {
        // given
        long invalidId = 0;
        // when & then
        RestAssured.given().log().all()
                .when().delete("/themes/" + invalidId)
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
        Long themeId = roomTheme.getId();
        // when
        Response deleteResponse = RestAssured.given().log().all()
                .when().delete("/themes/" + themeId)
                .then().log().all().extract().response();
        // then
        Response reservationResponse = RestAssured.given().log().all()
                .when().get("reservations")
                .then().log().all().extract().response();
        assertAll(
                () -> assertThat(deleteResponse.statusCode())
                        .isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(reservationResponse.jsonPath().getList(".")).isEmpty()
        );
    }
}
