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
import roomescape.service.AuthService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationApiTest {

    private static final String FUTURE_DATE_TEXT = LocalDate.now().plusDays(1).toString();
    private static final String PAST_DATE_TEXT = LocalDate.now().minusDays(1).toString();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AuthService authService;

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

    @DisplayName("저장된 모든 예약을 응답한다.")
    @Test
    void canResponseAllReservations() {
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(11, 0));

        reservationTimeRepository.add(reservationTime1);
        reservationTimeRepository.add(reservationTime2);

        Theme theme1 = new Theme(1L, "이름1", "설명1", "썸네일1");
        Theme theme2 = new Theme(2L, "이름2", "설명2", "썸네일2");

        themeRepository.add(theme1);
        themeRepository.add(theme2);

        Member member1 = new Member(1L, "랜디", "이메일", "비밀번호", "ADMIN");
        Member member2 = new Member(2L, "아마", "이메일", "비밀번호", "ADMIN");

        memberRepository.add(member1);
        memberRepository.add(member2);

        reservationRepository.add(
                Reservation.createWithoutId(member1, LocalDate.of(2025, 5, 5), reservationTime1, theme1));
        reservationRepository.add(
                Reservation.createWithoutId(member2, LocalDate.of(2025, 5, 5), reservationTime2, theme2));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("예약을 추가할 수 있다.")
    @Test
    void canCreateReservation() {
        Member member = new Member(1L, "아마", "이메일", "비밀번호", "ADMIN");
        Theme theme = new Theme(1L, "이름1", "설명1", "썸네일1");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        themeRepository.add(theme);
        reservationTimeRepository.add(reservationTime);
        memberRepository.add(member);

        Map<String, Object> params = new HashMap<>();
        params.put("date", FUTURE_DATE_TEXT);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", authService.makeToken(member))
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("location", "reservations/1");
    }

    @DisplayName("유효하지 않은 입력값으로 예약 추가가 불가능하다.")
    @Test
    void cannotCreateReservationsWhenInvalidRequest() {
        Member member = new Member(1L, "아마", "이메일", "비밀번호", "ADMIN");
        Theme theme = new Theme(1L, "이름1", "설명1", "썸네일1");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        themeRepository.add(theme);
        reservationTimeRepository.add(reservationTime);
        memberRepository.add(member);

        Map<String, Object> params = new HashMap<>();
        params.put("date", "");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", authService.makeToken(member))
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("과거의 날짜와 시간으로는 예약이 불가능하다")
    @Test
    void cannotCreateReservationsWhenPastRequest() {
        Member member = new Member(1L, "아마", "이메일", "비밀번호", "ADMIN");
        Theme theme = new Theme(1L, "이름1", "설명1", "썸네일1");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        themeRepository.add(theme);
        reservationTimeRepository.add(reservationTime);
        memberRepository.add(member);

        Map<String, Object> params = new HashMap<>();
        params.put("date", PAST_DATE_TEXT);
        params.put("timeId", 1);
        params.put("themeId", 1);
        params.put("memberId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", authService.makeToken(member))
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("중복 예약을 추가할 수 없다.")
    @Test
    void cannotCreateReservationsWhenDuplicatedTime() {
        Member member = new Member(1L, "아마", "이메일", "비밀번호", "ADMIN");
        Theme theme = new Theme(1L, "이름1", "설명1", "썸네일1");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        themeRepository.add(theme);
        reservationTimeRepository.add(reservationTime);
        memberRepository.add(member);

        jdbcTemplate.update("insert into reservation (date, time_id, theme_id, member_id) values (?, ?, ?, ?)",
                FUTURE_DATE_TEXT, 1, 1, 1);

        reservationRepository.add(
                Reservation.createWithoutId(member, LocalDate.now().plusDays(1), reservationTime, theme));

        Map<String, Object> params = new HashMap<>();
        params.put("date", FUTURE_DATE_TEXT);
        params.put("timeId", 1);
        params.put("themeId", 1);
        params.put("memberId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", authService.makeToken(member))
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("Id를 통해 예약을 삭제할 수 있다.")
    @Test
    void canDeleteReservationById() {
        Member member = new Member(1L, "아마", "이메일", "비밀번호", "ADMIN");
        Theme theme = new Theme(1L, "이름1", "설명1", "썸네일1");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        themeRepository.add(theme);
        reservationTimeRepository.add(reservationTime);
        memberRepository.add(member);
        reservationRepository.add(
                Reservation.createWithoutId(member, LocalDate.now().plusDays(1), reservationTime, theme));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 예약을 삭제할 수 없다.")
    @Test
    void cannotDeleteReservationByIdWhenNotExist() {
        Member member = new Member(1L, "아마", "이메일", "비밀번호", "ADMIN");
        Theme theme = new Theme(1L, "이름1", "설명1", "썸네일1");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        themeRepository.add(theme);
        reservationTimeRepository.add(reservationTime);
        memberRepository.add(member);
        reservationRepository.add(
                Reservation.createWithoutId(member, LocalDate.now().plusDays(1), reservationTime, theme));
        
        RestAssured.given().log().all()
                .when().delete("/reservations/2")
                .then().log().all()
                .statusCode(404);
    }
}
