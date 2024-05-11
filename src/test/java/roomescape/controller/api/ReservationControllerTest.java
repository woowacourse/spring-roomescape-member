package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.token.TokenProvider;
import roomescape.model.*;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @DisplayName("예약 목록 조회")
    @Test
    void getReservations() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusMonths(2), reservationTime, theme));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusMonths(1), reservationTime, theme));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("사용자 예약 추가 및 삭제")
    @TestFactory
    Stream<DynamicTest> userSaveAndDeleteReservation() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(member);
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));

        return Stream.of(
                dynamicTest("예약을 추가한다", () -> {
                    final Map<String, Object> params = Map.of(
                            "date", LocalDate.now().plusMonths(1),
                            "timeId", reservationTime.getId(),
                            "themeId", theme.getId());

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .body(params)
                            .when().post("/reservations")
                            .then().log().all()
                            .statusCode(201)
                            .header("Location", "/reservations/1");
                }),
                dynamicTest("예약을 삭제한다", () ->
                        RestAssured.given().log().all()
                                .when().delete("/reservations/1")
                                .then().log().all()
                                .statusCode(204)
                )
        );
    }

    @DisplayName("관리자 권한으로 예약 추가")
    @Test
    void adminSaveReservation() {
        final Member admin = memberRepository.save(new Member("고구마", Role.ADMIN, "111@aaa.com", "abc1234"));
        final Member member = memberRepository.save(new Member("감자", Role.USER, "222@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(admin);
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));

        final Map<String, Object> params = Map.of(
                "memberId", member.getId(),
                "date", LocalDate.now().plusMonths(1),
                "timeId", reservationTime.getId(),
                "themeId", theme.getId());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/1");
    }

    @DisplayName("일반 유저가 관리자용 예약 추가 호출 시 Forbidden 반환")
    @Test
    void forbiddenAdminSaveReservation() {
        memberRepository.save(new Member("고구마", Role.ADMIN, "111@aaa.com", "abc1234"));
        final Member member = memberRepository.save(new Member("감자", Role.USER, "222@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(member);
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));

        final Map<String, Object> params = Map.of(
                "memberId", member.getId(),
                "date", LocalDate.now().plusMonths(1),
                "timeId", reservationTime.getId(),
                "themeId", theme.getId());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(403)
                .body(equalTo(String.format("관리자 권한이 없는 사용자입니다. {id: %d, role: %s}", member.getId(), member.getRole().name())));
    }

    @DisplayName("예약 날짜가 비어 있는 예약 추가 시 BadRequest 반환")
    @Test
    void blankReservationDate() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(member);
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));

        final Map<String, Object> params = Map.of(
                "name", "감자",
                "date", "",
                "timeId", reservationTime.getId(),
                "themeId", theme.getId());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("date", equalTo("예약 날짜가 비어 있습니다."));
    }

    @DisplayName("예약 테마 아아디가 비어 있는 예약 추가 시 BadRequest 반환")
    @Test
    void blankReservationThemeId() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(member);
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));

        final Map<String, Object> params = Map.of(
                "name", "감자",
                "date", LocalDate.now().plusMonths(1),
                "timeId", reservationTime.getId(),
                "themeId", "");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("themeId", equalTo("예약 테마 아이디가 비어 있습니다."));
    }

    @DisplayName("예약 시간 아아디가 비어 있는 예약 추가 시 BadRequest 반환")
    @Test
    void blankReservationTimeId() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(member);
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));

        final Map<String, Object> params = Map.of(
                "name", "",
                "date", LocalDate.now().plusMonths(1),
                "timeId", "",
                "themeId", theme.getId());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("timeId", equalTo("예약 시간 아이디가 비어 있습니다."));
    }

    @DisplayName("유효하지 않은 날짜 형식 입력 시 BadRequest 반환")
    @ParameterizedTest
    @ValueSource(strings = {"2099.22.11", "2022", "abc"})
    void invalidDateFormat(final String date) {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(member);
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final Map<String, Object> params = Map.of(
                "name", "브라운",
                "date", date,
                "timeId", reservationTime.getId(),
                "themeId", theme.getId());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo(String.format("잘못된 날짜 혹은 시간 입력 형식입니다. (%s)", date)));
    }
}
