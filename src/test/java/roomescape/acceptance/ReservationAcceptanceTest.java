package roomescape.acceptance;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationAcceptanceTest {

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

    @DisplayName("사용자 권한으로 예약할 수 있다")
    @Test
    void when_postReservationWithUser_then_created() {
        // given
        String token = tokenProvider.createToken(UserFixture.member);

        Map<?, ?> request = Map.of(
                "date", UserFixture.tomorrow.toString(),
                "timeId", 2,
                "themeId", 2);

        // when
        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().response();

        // then
        JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(jsonPath.getString("member.email")).isEqualTo(UserFixture.email),
                () -> assertThat(jsonPath.getString("date")).isEqualTo(UserFixture.tomorrow.toString()),
                () -> assertThat(jsonPath.getString("theme.name")).isEqualTo(UserFixture.themeName)
        );
    }

    @DisplayName("어드민 권한으로 예약할 수 있다")
    @Test
    void when_postReservationWithAdmin_then_created() {
        // given
        String token = tokenProvider.createToken(AdminFixture.member);
        doReturn(Optional.of(token)).when(tokenProvider).extractToken(any());

        Map<?, ?> request = Map.of(
                "memberId", 1,
                "date", AdminFixture.tomorrow.toString(),
                "timeId", 1,
                "themeId", 1);

        // when
        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().response();

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getString("member.name")).isEqualTo(AdminFixture.name),
                () -> assertThat(response.jsonPath().getString("date")).isEqualTo(AdminFixture.tomorrow.toString()),
                () -> assertThat(response.jsonPath().getString("theme.name")).isEqualTo(AdminFixture.themeName)
        );
    }

    private static class UserFixture {
        public static final LocalDate tomorrow = LocalDate.now().plusDays(2);
        public static final LocalTime timeAfterOneHour = LocalTime.now().plusHours(1);
        public static final String name = "망쵸";
        public static final String themeName = "망쵸는 망쵸다";
        public static final String email = "mangcho@woowa.net";

        public static final Member member = new Member(
                2L,
                name,
                "mangcho@woowa.net",
                "nothing",
                "MEMBER");
        public static final Theme theme = new Theme(
                2L,
                themeName,
                "설명서",
                "https://i.postimg.cc/cLqW2JLB/theme-SOS-SOS.jpg");
        public static final ReservationTime time = new ReservationTime(2L, timeAfterOneHour);
    }

    private static class AdminFixture {
        public static final LocalDate tomorrow = LocalDate.now().plusDays(1);
        public static final LocalTime timeAfterOneHour = LocalTime.now().plusHours(1);
        public static final String name = "피케이";
        public static final String themeName = "피케이는 피케이다";

        public static final Member member = new Member(
                1L, name,
                "pkpkpkpk@woowa.net",
                "anything",
                "ADMIN");
        public static final Theme theme = new Theme(
                1L,
                themeName,
                "설명서",
                "https://i.postimg.cc/cLqW2JLB/theme-SOS-SOS.jpg");
        public static final ReservationTime time = new ReservationTime(1L, timeAfterOneHour);
    }
}
