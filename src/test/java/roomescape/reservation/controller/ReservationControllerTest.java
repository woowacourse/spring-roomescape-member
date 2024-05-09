package roomescape.reservation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ReservationControllerTest {

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private MemberDao memberDao;

    @LocalServerPort
    private int port;

    private String accessTokenCookie;

    @BeforeEach
    void init() {
        String email = "test@email.com";
        String password = "12341234";
        memberDao.insert(new Member("이름", email, password));

        Map<String, String> loginParams = Map.of(
                "email", email,
                "password", password
        );

        accessTokenCookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];
    }

    @Test
    @DisplayName("처음으로 등록하는 예약의 id는 1이다.")
    void firstPost() {
        reservationTimeDao.insert(new ReservationTime(LocalTime.of(17, 30)));
        themeDao.insert(new Theme("테마명", "설명", "썸네일URL"));

        Map<String, String> reservationParams = Map.of(
                "name", "썬",
                "date", LocalDate.now().plusDays(1L).toString(),
                "timeId", "1",
                "themeId", "1"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .header("Cookie", accessTokenCookie)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("data.id", is(1))
                .header("Location", "/reservations/1");
    }

    @Test
    @DisplayName("전체 예약정보를 조회한다.")
    void readEmptyReservations() {
        // given
        ReservationTime reservationTime = reservationTimeDao.insert(new ReservationTime(LocalTime.of(17, 30)));
        Theme theme = themeDao.insert(new Theme("테마명", "설명", "썸네일URL"));
        Member member = memberDao.insert(new Member("name", "email@email.com", "password"));

        // when
        reservationDao.insert(new Reservation(LocalDate.now(), reservationTime, theme, member));
        reservationDao.insert(new Reservation(LocalDate.now().plusDays(1), reservationTime, theme, member));
        reservationDao.insert(new Reservation(LocalDate.now().plusDays(2), reservationTime, theme, member));

        // then
        RestAssured.given().log().all()
                .port(port)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("data.reservations.size()", is(3));
    }

    @Test
    @DisplayName("예약 정보를 삭제한다.")
    void readReservationsSizeAfterPostAndDelete() {
        // given
        ReservationTime reservationTime = reservationTimeDao.insert(new ReservationTime(LocalTime.of(17, 30)));
        Theme theme = themeDao.insert(new Theme("테마명", "설명", "썸네일URL"));
        Member member = memberDao.insert(new Member("name", "email@email.com", "password"));

        Reservation reservation = reservationDao.insert(new Reservation(LocalDate.now(), reservationTime, theme, member));

        // when
        RestAssured.given().log().all()
                .port(port)
                .when().delete("/reservations/" + reservation.getId())
                .then().log().all()
                .statusCode(204);

        // then
        assertThat(reservationDao.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("특정 날짜의 특정 테마 예약 현황을 조회한다.")
    void readReservationByDateAndThemeId() {
        // given
        LocalDate today = LocalDate.now();
        ReservationTime reservationTime1 = reservationTimeDao.insert(new ReservationTime(LocalTime.of(17, 00)));
        ReservationTime reservationTime2 = reservationTimeDao.insert(new ReservationTime(LocalTime.of(17, 30)));
        ReservationTime reservationTime3 = reservationTimeDao.insert(new ReservationTime(LocalTime.of(18, 30)));
        Theme theme = themeDao.insert(new Theme("테마명1", "설명", "썸네일URL"));
        Member member = memberDao.insert(new Member("name", "email@email.com", "password"));

        reservationDao.insert(new Reservation(today.plusDays(1), reservationTime1, theme, member));
        reservationDao.insert(new Reservation(today.plusDays(1), reservationTime2, theme, member));
        reservationDao.insert(new Reservation(today.plusDays(1), reservationTime3, theme, member));

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .when().get("/reservations/themes/1/times?date=" + today.plusDays(1))
                .then().log().all()
                .statusCode(200)
                .body("data.reservationTimes.size()", is(3));
    }

    @ParameterizedTest
    @MethodSource("requestValidateSource")
    @DisplayName("예약 생성 시, 요청 값에 공백 또는 null이 포함되어 있으면 400 에러를 발생한다.")
    void validateBlankRequest(Map<String, String> invalidRequestBody) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(invalidRequestBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    private static Stream<Map<String, String>> requestValidateSource() {
        return Stream.of(
                Map.of("timeId", "1",
                        "themeId", "1"),
                Map.of("date", LocalDate.now().plusDays(1L).toString(),
                        "themeId", "1"),
                Map.of("date", LocalDate.now().plusDays(1L).toString(),
                        "timeId", "1"),
                Map.of("date", LocalDate.now().plusDays(1L).toString(),
                        "timeId", "1",
                        "themeId", " ")
        );
    }

    @Test
    @DisplayName("예약 생성 시, 정수 요청 데이터에 문자가 입력되어오면 400 에러를 발생한다.")
    void validateRequestDataFormat() {
        Map<String, String> invalidTypeRequestBody = Map.of(
                "date", LocalDate.now().plusDays(1L).toString(),
                "timeId", "1",
                "themeId", "한글"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(invalidTypeRequestBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
