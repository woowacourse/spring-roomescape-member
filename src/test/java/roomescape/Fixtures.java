package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.MemberTokenConverter;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.service.member.dto.MemberLoginRequest;

public class Fixtures {
    public static final String AUTH_COOKIE_NAME = MemberTokenConverter.AUTH_COOKIE_NAME;

    public static final LocalDate DATE_AFTER_6_MONTH_LATER = LocalDate.now().plusMonths(6);

    public static final LocalTime TIME_10_10 = LocalTime.of(10, 10);

    public static final ReservationTime reservationTimeFixture = new ReservationTime(
            1L,
            LocalTime.of(10, 10)
    );

    public static final Theme themeFixture = new Theme(
            1L,
            "공포",
            "완전 무서운 테마",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    );

    public static final Member memberFixture = new Member(
            1L,
            "클로버",
            "clover@me.com",
            "11111"
    );

    public static final Reservation reservationFixture = new Reservation(
            1L, memberFixture, LocalDate.now().plusMonths(6),
            reservationTimeFixture, themeFixture
    );

    public static final List<Theme> themeFixtures = List.of(
            new Theme(1L, "테마 이름 1", "테마 설명 1", "1"),
            new Theme(2L, "테마 이름 2", "테마 설명 2", "2"),
            new Theme(3L, "테마 이름 3", "테마 설명 3", "3"),
            new Theme(4L, "테마 이름 4", "테마 설명 4", "4"),
            new Theme(5L, "테마 이름 5", "테마 설명 5", "5"),
            new Theme(6L, "테마 이름 6", "테마 설명 6", "6"),
            new Theme(7L, "테마 이름 7", "테마 설명 7", "7"),
            new Theme(8L, "테마 이름 8", "테마 설명 8", "8"),
            new Theme(9L, "테마 이름 9", "테마 설명 9", "9"),
            new Theme(10L, "테마 이름 10", "테마 설명 10", "10"),
            new Theme(11L, "테마 이름 11", "테마 설명 11", "11")
    );

    // 5번 테마(7회), 1번 테마(5회), 2번 테마(4회), 3번 테마(3회), 4번 테마(1회)
    public static final List<Reservation> reservationFixturesForPopularTheme = List.of(
            // 1번 테마
            new Reservation(1L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(0)),
            new Reservation(2L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(0)),
            new Reservation(3L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(0)),
            new Reservation(4L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(0)),
            new Reservation(5L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(0)),
            // 2번 테마
            new Reservation(6L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(1)),
            new Reservation(7L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(1)),
            new Reservation(8L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(1)),
            new Reservation(9L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(1)),
            // 3번 테마
            new Reservation(10L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(2)),
            new Reservation(11L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(2)),
            new Reservation(12L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(2)),
            // 4번 테마
            new Reservation(13L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(3)),
            // 5번 테마
            new Reservation(14L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(15L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(16L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(17L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(18L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(19L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(20L, memberFixture, LocalDate.now().minusDays(3), null, themeFixtures.get(4))
    );

    public static String login(String email, String password) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberLoginRequest(email, password))
                .when().post("/login")
                .then().log().all()
                .extract()
                .cookie(AUTH_COOKIE_NAME);
    }
}
