package roomescape;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Fixtures {

    public static ReservationTime reservationTimeFixture = new ReservationTime(
            1L,
            LocalTime.of(10, 10)
    );

    public static final Theme themeFixture = new Theme(
            1L,
            "공포",
            "완전 무서운 테마",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    );

    public static Reservation reservationFixture = new Reservation(
            1L, "클로버", LocalDate.now().plusMonths(6),
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

    public static final List<Reservation> reservationFixturesForPopularTheme = List.of(
            new Reservation(1L, "예약자명1", LocalDate.now().minusDays(3), null, themeFixtures.get(0)),
            new Reservation(2L, "예약자명2", LocalDate.now().minusDays(3), null, themeFixtures.get(0)),
            new Reservation(3L, "예약자명3", LocalDate.now().minusDays(3), null, themeFixtures.get(0)),
            new Reservation(4L, "예약자명4", LocalDate.now().minusDays(3), null, themeFixtures.get(0)),
            new Reservation(5L, "예약자명5", LocalDate.now().minusDays(3), null, themeFixtures.get(0)),
            new Reservation(6L, "예약자명6", LocalDate.now().minusDays(3), null, themeFixtures.get(1)),
            new Reservation(7L, "예약자명7", LocalDate.now().minusDays(3), null, themeFixtures.get(1)),
            new Reservation(8L, "예약자명8", LocalDate.now().minusDays(3), null, themeFixtures.get(1)),
            new Reservation(9L, "예약자명9", LocalDate.now().minusDays(3), null, themeFixtures.get(1)),
            new Reservation(10L, "예약자명10", LocalDate.now().minusDays(3), null, themeFixtures.get(2)),
            new Reservation(11L, "예약자명11", LocalDate.now().minusDays(3), null, themeFixtures.get(2)),
            new Reservation(12L, "예약자명12", LocalDate.now().minusDays(3), null, themeFixtures.get(2)),
            new Reservation(13L, "예약자명13", LocalDate.now().minusDays(3), null, themeFixtures.get(3)),
            new Reservation(14L, "예약자명14", LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(15L, "예약자명15", LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(16L, "예약자명16", LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(17L, "예약자명17", LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(18L, "예약자명18", LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(19L, "예약자명19", LocalDate.now().minusDays(3), null, themeFixtures.get(4)),
            new Reservation(20L, "예약자명20", LocalDate.now().minusDays(3), null, themeFixtures.get(4))
    );



}
