package roomescape.integration;

import static org.hamcrest.Matchers.is;
import static roomescape.exception.ExceptionType.DUPLICATE_RESERVATION;
import static roomescape.exception.ExceptionType.PAST_TIME_RESERVATION;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.Fixture;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.JwtGenerator;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class ReservationControllerTest {


    @LocalServerPort
    int port;

    @Autowired
    private JwtGenerator JWT_GENERATOR;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private MemberRepository memberRepository;
    private Theme defaultTheme = new Theme("theme1", "description", "thumbnail");
    private ReservationTime defaultTime = new ReservationTime(LocalTime.of(11, 30));
    private Member defaultMember = Fixture.defaultMember;
    private String token;

    @BeforeEach
    void initData() {
        RestAssured.port = port;
        token = JWT_GENERATOR.generateWith(
                Map.of(
                        "id", defaultMember.getId(),
                        "name", defaultMember.getName()
                )
        );

        defaultTheme = themeRepository.save(defaultTheme);
        defaultTime = reservationTimeRepository.save(defaultTime);
        defaultMember = memberRepository.save(defaultMember);
    }

    @DisplayName("예약이 10개 존재할 때")
    @Nested
    class ExistReservationTest {

        @BeforeEach
        void initData() {
            reservationRepository.save(
                    new Reservation(LocalDate.now().minusDays(5), defaultTime, defaultTheme,
                            defaultMember.getLoginMember()));
            reservationRepository.save(
                    new Reservation(LocalDate.now().minusDays(4), defaultTime, defaultTheme,
                            defaultMember.getLoginMember()));
            reservationRepository.save(
                    new Reservation(LocalDate.now().minusDays(3), defaultTime, defaultTheme,
                            defaultMember.getLoginMember()));
            reservationRepository.save(
                    new Reservation(LocalDate.now().minusDays(2), defaultTime, defaultTheme,
                            defaultMember.getLoginMember()));
            reservationRepository.save(
                    new Reservation(LocalDate.now().minusDays(1), defaultTime, defaultTheme,
                            defaultMember.getLoginMember()));

            reservationRepository.save(
                    new Reservation(LocalDate.now(), defaultTime, defaultTheme, defaultMember.getLoginMember()));
            reservationRepository.save(
                    new Reservation(LocalDate.now().plusDays(1), defaultTime, defaultTheme,
                            defaultMember.getLoginMember()));
            reservationRepository.save(
                    new Reservation(LocalDate.now().plusDays(2), defaultTime, defaultTheme,
                            defaultMember.getLoginMember()));
            reservationRepository.save(
                    new Reservation(LocalDate.now().plusDays(3), defaultTime, defaultTheme,
                            defaultMember.getLoginMember()));
            reservationRepository.save(
                    new Reservation(LocalDate.now().plusDays(4), defaultTime, defaultTheme,
                            defaultMember.getLoginMember()));

        }

        @DisplayName("존재하는 모든 예약을 조회할 수 있다.")
        @Test
        void getReservationTest() {
            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(10));
        }

        @DisplayName("예약을 하나 생성할 수 있다.")
        @Test
        void createReservationTest() {
            Map<String, Object> reservationParam = Map.of(
                    "date", LocalDate.now().plusMonths(1).toString(),
                    "timeId", "1",
                    "themeId", "1");

            RestAssured.given().log().all()
                    .when()
                    .cookie("token", token)
                    .contentType(ContentType.JSON)
                    .body(reservationParam)
                    .post("/reservations")
                    .then().log().all()
                    .statusCode(201)
                    .body("id", is(11),
                            "user.name", is(defaultMember.getName()),
                            "date", is(reservationParam.get("date")),
                            "time.startAt", is(defaultTime.getStartAt().toString()),
                            "theme.name", is(defaultTheme.getName()));

            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(11));
        }

        @DisplayName("지난 시간에 예약을 생성할 수 없다.")
        @Test
        void createPastReservationTest() {
            Map<String, Object> reservationParam = Map.of(
                    "date", LocalDate.now().minusMonths(1).toString(),
                    "timeId", "1",
                    "themeId", "1");

            RestAssured.given().log().all()
                    .when()
                    .cookie("token", token)
                    .contentType(ContentType.JSON)
                    .body(reservationParam)
                    .post("/reservations")
                    .then().log().all()
                    .statusCode(400)
                    .body("message", is(PAST_TIME_RESERVATION.getMessage()));
        }

        @DisplayName("중복된 예약을 생성할 수 없다.")
        @Test
        void duplicatedReservationTest() {
            RestAssured.given().log().all()
                    .when()
                    .cookie("token", token)
                    .contentType(ContentType.JSON)
                    .body(Map.of(
                            "date", LocalDate.now().toString(),
                            "timeId", 1,
                            "themeId", 1))
                    .post("/reservations")
                    .then().log().all()
                    .statusCode(400)
                    .body("message", is(DUPLICATE_RESERVATION.getMessage()));
        }

        @DisplayName("예약을 하나 삭제할 수 있다.")
        @Test
        void deleteReservationTest() {
            RestAssured.given().log().all()
                    .when().delete("/reservations/1")
                    .then().log().all()
                    .statusCode(204);

            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(9));
        }
    }
}
