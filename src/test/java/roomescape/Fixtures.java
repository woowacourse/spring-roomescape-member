package roomescape;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Fixtures {

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

    public static final Reservation reservationFixture = new Reservation(
            1L, "클로버", LocalDate.now().plusMonths(6),
            reservationTimeFixture, themeFixture
    );

    public static final List<Theme> themeFixtures = List.of(
            new Theme(1L, "테마 이름 1", "테마 설명 1", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(2L, "테마 이름 2", "테마 설명 2", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(3L, "테마 이름 3", "테마 설명 3", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(4L, "테마 이름 4", "테마 설명 4", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(5L, "테마 이름 5", "테마 설명 5", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(6L, "테마 이름 6", "테마 설명 6", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(7L, "테마 이름 7", "테마 설명 7", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(8L, "테마 이름 8", "테마 설명 8", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(9L, "테마 이름 9", "테마 설명 9", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(10L, "테마 이름 10", "테마 설명 10", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(11L, "테마 이름 11", "테마 설명 11", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg1")
    );

    public static final List<ReservationTime> timeFixtures = List.of(
            new ReservationTime(1L, LocalTime.of(10, 10)),
            new ReservationTime(2L, LocalTime.of(10, 15)),
            new ReservationTime(3L, LocalTime.of(10, 20)),
            new ReservationTime(4L, LocalTime.of(10, 25)),
            new ReservationTime(5L, LocalTime.of(10, 30))
    );

    public static final List<Reservation> reservationFixturesForPopularTheme = List.of(
            new Reservation(1L, "예약자명1", LocalDate.now().minusDays(3), timeFixtures.get(0), themeFixtures.get(0)),
            new Reservation(2L, "예약자명2", LocalDate.now().minusDays(3), timeFixtures.get(1), themeFixtures.get(0)),
            new Reservation(3L, "예약자명3", LocalDate.now().minusDays(3), timeFixtures.get(2), themeFixtures.get(0)),
            new Reservation(4L, "예약자명4", LocalDate.now().minusDays(3), timeFixtures.get(3), themeFixtures.get(0)),
            new Reservation(5L, "예약자명5", LocalDate.now().minusDays(3), timeFixtures.get(4), themeFixtures.get(0)),
            new Reservation(6L, "예약자명6", LocalDate.now().minusDays(3), timeFixtures.get(0), themeFixtures.get(1)),
            new Reservation(7L, "예약자명7", LocalDate.now().minusDays(3), timeFixtures.get(1), themeFixtures.get(1)),
            new Reservation(8L, "예약자명8", LocalDate.now().minusDays(3), timeFixtures.get(2), themeFixtures.get(1)),
            new Reservation(9L, "예약자명9", LocalDate.now().minusDays(3), timeFixtures.get(3), themeFixtures.get(1)),
            new Reservation(10L, "예약자명10", LocalDate.now().minusDays(3), timeFixtures.get(0), themeFixtures.get(2)),
            new Reservation(11L, "예약자명11", LocalDate.now().minusDays(3), timeFixtures.get(1), themeFixtures.get(2)),
            new Reservation(12L, "예약자명12", LocalDate.now().minusDays(3), timeFixtures.get(2), themeFixtures.get(2)),
            new Reservation(13L, "예약자명13", LocalDate.now().minusDays(3), timeFixtures.get(0), themeFixtures.get(3)),
            new Reservation(14L, "예약자명14", LocalDate.now().minusDays(3), timeFixtures.get(0), themeFixtures.get(4)),
            new Reservation(15L, "예약자명15", LocalDate.now().minusDays(3), timeFixtures.get(1), themeFixtures.get(4)),
            new Reservation(16L, "예약자명16", LocalDate.now().minusDays(3), timeFixtures.get(2), themeFixtures.get(4)),
            new Reservation(17L, "예약자명17", LocalDate.now().minusDays(3), timeFixtures.get(3), themeFixtures.get(4)),
            new Reservation(18L, "예약자명18", LocalDate.now().minusDays(3), timeFixtures.get(4), themeFixtures.get(4)),
            new Reservation(19L, "예약자명19", LocalDate.now().minusDays(3), timeFixtures.get(0), themeFixtures.get(4)),
            new Reservation(20L, "예약자명20", LocalDate.now().minusDays(3), timeFixtures.get(1), themeFixtures.get(4))
    );
}
