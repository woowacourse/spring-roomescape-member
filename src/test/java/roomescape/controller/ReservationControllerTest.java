package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

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
    private ReservationRepository reservationRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("처음으로 등록하는 예약의 id는 1이다.")
    void firstPost() {
        timeRepository.insert(new Time(LocalTime.of(17, 30)));
        themeRepository.insert(new Theme("테마명", "설명", "썸네일URL"));

        Map<String, String> reservationParams = Map.of(
                "name", "썬",
                "date", LocalDate.now().plusDays(1L).toString(),
                "timeId", "1",
                "themeId", "1"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
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
        Time time = timeRepository.insert(new Time(LocalTime.of(17, 30)));
        Theme theme = themeRepository.insert(new Theme("테마명", "설명", "썸네일URL"));

        // when
        reservationRepository.insert(new Reservation("예약자1", LocalDate.now(), time, theme));
        reservationRepository.insert(new Reservation("예약자2", LocalDate.now().plusDays(1), time, theme));
        reservationRepository.insert(new Reservation("예약자3", LocalDate.now().plusDays(2), time, theme));

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
        Time time = timeRepository.insert(new Time(LocalTime.of(17, 30)));
        Theme theme = themeRepository.insert(new Theme("테마명", "설명", "썸네일URL"));

        Reservation reservation = reservationRepository.insert(new Reservation("예약자1", LocalDate.now(), time, theme));

        // when
        RestAssured.given().log().all()
                .port(port)
                .when().delete("/reservations/" + reservation.getId())
                .then().log().all()
                .statusCode(204);

        // then
        assertThat(reservationRepository.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("특정 날짜의 특정 테마 예약 현황을 조회한다.")
    void readReservationByDateAndThemeId() {
        // given
        LocalDate today = LocalDate.now();
        Time time1 = timeRepository.insert(new Time(LocalTime.of(17, 00)));
        Time time2 = timeRepository.insert(new Time(LocalTime.of(17, 30)));
        Time time3 = timeRepository.insert(new Time(LocalTime.of(18, 30)));
        Theme theme = themeRepository.insert(new Theme("테마명1", "설명", "썸네일URL"));

        reservationRepository.insert(new Reservation("예약자1", today.plusDays(1), time1, theme));
        reservationRepository.insert(new Reservation("예약자1", today.plusDays(1), time2, theme));
        reservationRepository.insert(new Reservation("예약자1", today.plusDays(1), time3, theme));

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
                Map.of("name", "썬",
                        "date", LocalDate.now().plusDays(1L).toString(),
                        "themeId", "1"),
                Map.of("name", " ",
                        "date", LocalDate.now().plusDays(1L).toString(),
                        "timeId", "1",
                        "themeId", "1"),
                Map.of("name", "",
                        "date", LocalDate.now().plusDays(1L).toString(),
                        "timeId", " ",
                        "themeId", "1")
        );
    }

    @Test
    @DisplayName("예약 생성 시, 정수 요청 데이터에 문자가 입력되어오면 400 에러를 발생한다.")
    void validateRequestDataFormat() {
        Map<String, String> invalidTypeRequestBody = Map.of(
                "name", "썬",
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
