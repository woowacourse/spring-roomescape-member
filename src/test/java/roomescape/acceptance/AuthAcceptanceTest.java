package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.ReservationTimeRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.web.api.token.TokenProvider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;

    @SpyBean
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        initAdminData();
        initUserData();
    }

    private void initAdminData() {
        memberRepository.save(AdminFixture.member);
        reservationTimeRepository.save(AdminFixture.time);
        themeRepository.save(AdminFixture.theme);
    }

    private void initUserData() {
        memberRepository.save(UserFixture.member);
        reservationTimeRepository.save(UserFixture.time);
        themeRepository.save(UserFixture.theme);
    }

    @Test
    @DisplayName("권한이 없으면 POST /reservations에 접근할 수 없다")
    void when_hasNoAuth_then_canNotAccessURI() {
        RestAssured.given().log().all()
                .when().post("/reservations")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("멤버 권한이 있으면 POST /reservations에 접근할 수 있다")
    @Test
    void when_hasMemberAuth_then_canAccessURI() {
        // given
        String token = tokenProvider.createToken(UserFixture.member);

        Map<String, Object> request = Map.of(
                "date", UserFixture.tomorrow.toString(),
                "timeId", 2,
                "themeId", 2);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("어드민 권한이 있으면 POST /reservations에 접근할 수 있다")
    @Test
    void when_hasAdminAuth_then_canAccessURI() {
        // given
        String token = tokenProvider.createToken(AdminFixture.member);

        Map<String, Object> request = Map.of(
                "date", UserFixture.tomorrow.toString(),
                "timeId", 2,
                "themeId", 2);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("어드민 권한이 있으면 POST /admin/reservations에 접근할 수 있다")
    @Test
    void when_hasAdminAuth_then_canAccessAdminReservationURI() {
        // given
        String token = tokenProvider.createToken(AdminFixture.member);

        Map<String, Object> request = Map.of(
                "date", UserFixture.tomorrow.toString(),
                "timeId", 2,
                "themeId", 2);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("멤버 권한으로 GET /admin/reservations에 접근할 수 없다")
    @Test
    void when_hasMemberAuth_then_canNotAccessAdminURI() {
        // given
        String token = tokenProvider.createToken(UserFixture.member);

        // when & then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservations?memberId=1&themeId=1&dateFrom=2021-10-01&dateTo=2099-12-01")
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("어드민 권한이 있으면 GET /admin/reservations에 접근할 수 있다")
    @Test
    void when_hasAdminAuth_then_canAccessAdminURI() {
        // given
        String token = tokenProvider.createToken(AdminFixture.member);

        // when & then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservations?memberId=1&themeId=1&dateFrom=2021-10-01&dateTo=2099-12-01")
                .then().log().all()
                .statusCode(200);
    }

    @Disabled
    @DisplayName("권한이 없으면 DELETE /times/{id}에 접근할 수 없다")
    @Test
    void when_hasNoAuth_then_canNotAccessDeleteURI() {
        // when & then
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("멤버 권한이 있으면 DELETE /times/{id}에 접근할 수 있다")
    @Test
    void when_hasMemberAuth_then_canNotAccessDeleteURI() {
        // given
        String token = tokenProvider.createToken(UserFixture.member);

        // when & then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    private static class UserFixture {
        public static final LocalDate tomorrow = LocalDate.now().plusDays(2);
        public static final LocalTime timeAfterOneHour = LocalTime.now().plusHours(1);
        public static final String name = "망쵸";
        public static final String themeName = "망쵸는 망쵸다";
        public static final String email = "mangcho@woowa.net";
        public static final String auth = "MEMBER";
        public static final String password = "nothing";
        public static final Member member = new Member(2L, name, email, password, auth);
        public static final String description = "설명서";
        public static final String url = "https://i.postimg.cc/cLqW2JLB/theme-SOS-SOS.jpg";
        public static final Theme theme = new Theme(2L, themeName, description, url);
        public static final ReservationTime time = new ReservationTime(2L, timeAfterOneHour);
    }

    private static class AdminFixture {
        public static final LocalDate tomorrow = LocalDate.now().plusDays(1);
        public static final LocalTime timeAfterOneHour = LocalTime.now().plusHours(1);
        public static final String name = "피케이";
        public static final String themeName = "피케이는 피케이다";
        public static final String email = "pkpkpkpk@woowa.net";
        public static final String password = "anything";
        public static final String auth = "ADMIN";
        public static final Member member = new Member(1L, name, email, password, auth);
        public static final String description = "설명서";
        public static final String url = "https://i.postimg.cc/cLqW2JLB/theme-SOS-SOS.jpg";
        public static final Theme theme = new Theme(1L, themeName, description, url);
        public static final ReservationTime time = new ReservationTime(1L, timeAfterOneHour);
    }
}
