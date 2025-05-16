package roomescape;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeApiTest {

    private static final String FUTURE_DATE_TEXT = LocalDate.now().plusDays(1).toString();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ThemeRepository themeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");

        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("예약 가능한 시간을 추가할 수 있다.")
    @Test
    void canCreateReservationTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("location", "times/1");
    }

    @DisplayName("이미 존재하는 예약 가능 시간은 추가할 수 없다.")
    @Test
    void cannotCreateReservationTimeWhenExist() {
        reservationTimeRepository.add(ReservationTime.createWithoutId(LocalTime.of(10, 0)));

        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("유효하지 않은 요청을 예약 가능 시간을 추가할 수 없다")
    @Test
    void cannotCreateReservationTimeWhenInvalidRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 가능 시간을 조회할 수 있다")
    @Test
    void canResponseAllReservationTimes() {
        reservationTimeRepository.add(ReservationTime.createWithoutId(LocalTime.of(10, 0)));
        reservationTimeRepository.add(ReservationTime.createWithoutId(LocalTime.of(11, 0)));

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("예약 여부와 함께 예약 가능 시간을 조회할 수 있다")
    @Test
    void canResponseAvailableReservationTime() {
        Member member = new Member(1L, "아마", "이메일", "비밀번호", "ADMIN");
        Theme theme = new Theme(1L, "이름1", "설명1", "썸네일1");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        themeRepository.add(theme);
        reservationTimeRepository.add(reservationTime);
        memberRepository.add(member);

        reservationRepository.add(
                Reservation.createWithoutId(member, LocalDate.now().plusDays(1), reservationTime, theme));

        RestAssured.given().log().all()
                .when().get("/" + FUTURE_DATE_TEXT + "/1" + "/times")
                .then().log().all()
                .statusCode(200)
                .body("get(0).bookState", is(true));

        jdbcTemplate.update("delete from reservation");

        RestAssured.given().log().all()
                .when().get("/" + FUTURE_DATE_TEXT + "/1" + "/times")
                .then().log().all()
                .statusCode(200)
                .body("get(0).bookState", is(false));
    }

    @DisplayName("예약 가능한 시간을 삭제할 수 있다")
    @Test
    void canDeleteReservationTime() {
        reservationTimeRepository.add(ReservationTime.createWithoutId(LocalTime.of(10, 0)));

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 시간을 삭제할 수 없다.")
    @Test
    void cannotDeleteReservationTimeWhenNotExist() {
        reservationTimeRepository.add(ReservationTime.createWithoutId(LocalTime.of(10, 0)));

        RestAssured.given().log().all()
                .when().delete("/times/2")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("이미 해당 시간에 대해 예약 데이터가 존재한다면 삭제가 불가능하다")
    @Test
    void cannotDeleteReservationTimeWhenExistReservation() {
        Member member = new Member(1L, "아마", "이메일", "비밀번호", "ADMIN");
        Theme theme = new Theme(1L, "이름1", "설명1", "썸네일1");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        themeRepository.add(theme);
        reservationTimeRepository.add(reservationTime);
        memberRepository.add(member);

        reservationRepository.add(
                Reservation.createWithoutId(member, LocalDate.now().plusDays(1), reservationTime, theme));

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }
}
