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
public class ThemeApiTest {

    private static final String FUTURE_DATE_TEXT = LocalDate.now().plusDays(1).toString();
    private static final String PAST_DATE_TEXT = LocalDate.now().minusDays(1).toString();

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

    @DisplayName("테마를 추가할 수 있다.")
    @Test
    void canCreateTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "이름");
        params.put("description", "설명");
        params.put("thumbnail", "썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("location", "theme/1");
    }

    @DisplayName("유효하지 않은 요청으로는 테마를 추가할 수 없다.")
    @Test
    void cannotCreateThemeWhenInvalidRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "이름");
        params.put("description", "");
        params.put("thumbnail", "썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("모든 테마를 조회할 수 있다")
    @Test
    void canResponseAllTheme() {
        themeRepository.add(Theme.createWithoutId("인기테마1", "설명1", "썸네일1"));
        themeRepository.add(Theme.createWithoutId("인기테마2", "설명1", "썸네일1"));
        themeRepository.add(Theme.createWithoutId("인기테마3", "설명1", "썸네일1"));

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("인기 테마를 조회할 수 있다.")
    @Test
    void canResponseTopThemes() {
        themeRepository.add(Theme.createWithoutId("인기테마", "설명1", "썸네일1"));
        themeRepository.add(Theme.createWithoutId("평범테마", "설명2", "썸네일2"));
        themeRepository.add(Theme.createWithoutId("인기없는테마", "설명3", "썸네일3"));

        themeRepository.add(Theme.createWithoutId("테마1", "설명1", "썸네일1"));
        themeRepository.add(Theme.createWithoutId("테마2", "설명1", "썸네일1"));
        themeRepository.add(Theme.createWithoutId("테마3", "설명1", "썸네일1"));
        themeRepository.add(Theme.createWithoutId("테마4", "설명1", "썸네일1"));
        themeRepository.add(Theme.createWithoutId("테마5", "설명1", "썸네일1"));
        themeRepository.add(Theme.createWithoutId("테마6", "설명1", "썸네일1"));
        themeRepository.add(Theme.createWithoutId("테마7", "설명1", "썸네일1"));

        reservationTimeRepository.add(ReservationTime.createWithoutId(LocalTime.of(10, 0)));
        reservationTimeRepository.add(ReservationTime.createWithoutId(LocalTime.of(11, 0)));
        reservationTimeRepository.add(ReservationTime.createWithoutId(LocalTime.of(12, 0)));

        Member admin = Member.createMemberWithoutId("아마", "이메일", "비밀번호", "ADMIN");
        memberRepository.add(admin);

        Member member = memberRepository.findMemberById(1L).orElseThrow();
        Theme theme1 = themeRepository.findById(1L).orElseThrow();
        Theme theme2 = themeRepository.findById(2L).orElseThrow();
        Theme theme3 = themeRepository.findById(3L).orElseThrow();

        ReservationTime time1 = reservationTimeRepository.findById(1L).orElseThrow();
        ReservationTime time2 = reservationTimeRepository.findById(2L).orElseThrow();
        ReservationTime time3 = reservationTimeRepository.findById(3L).orElseThrow();

        reservationRepository.add(Reservation.createWithoutId(member, LocalDate.now().plusDays(1), time1, theme1));
        reservationRepository.add(Reservation.createWithoutId(member, LocalDate.now().plusDays(1), time2, theme1));
        reservationRepository.add(Reservation.createWithoutId(member, LocalDate.now().plusDays(1), time3, theme1));
        reservationRepository.add(Reservation.createWithoutId(member, LocalDate.now().plusDays(1), time1, theme2));
        reservationRepository.add(Reservation.createWithoutId(member, LocalDate.now().plusDays(1), time2, theme2));
        reservationRepository.add(Reservation.createWithoutId(member, LocalDate.now().plusDays(1), time1, theme3));

        RestAssured.given().log().all()
                .when().get("/themes/top")
                .then().log().all()
                .statusCode(200)
                .body("get(0).name", is("인기테마"))
                .body("get(1).name", is("평범테마"))
                .body("get(2).name", is("인기없는테마"))
                .body("size()", is(10));
    }

    @DisplayName("ID를 통해 테마를 삭제할 수 있다")
    @Test
    void canDeleteThemeById() {
        themeRepository.add(Theme.createWithoutId("테마1", "설명1", "썸네일1"));

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 테마를 삭제할 수 없다")
    @Test
    void cannotDeleteThemeByIdWhenNotExist() {
        themeRepository.add(Theme.createWithoutId("테마1", "설명1", "썸네일1"));

        RestAssured.given().log().all()
                .when().delete("/themes/2")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("이미 테마에 대한 예약이 존재한다면 해당 테마의 삭제가 불가능하다.")
    @Test
    void cannotDeleteThemeByIdWhenReservationExist() {
        Member member = new Member(1L, "아마", "이메일", "비밀번호", "ADMIN");
        Theme theme = new Theme(1L, "이름1", "설명1", "썸네일1");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        themeRepository.add(theme);
        reservationTimeRepository.add(reservationTime);
        memberRepository.add(member);
        reservationRepository.add(
                Reservation.createWithoutId(member, LocalDate.now().plusDays(1), reservationTime, theme));

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400);
    }
}
