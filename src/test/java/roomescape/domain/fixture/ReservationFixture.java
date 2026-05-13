package roomescape.domain.fixture;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public final class ReservationFixture {

    private ReservationFixture() {
    }

    public static Stream<Arguments> invalidReservationConstructor() {
        return Stream.of(
                Arguments.of(
                        null,
                        ThemeFixture.createDefaultTheme(),
                        ReservationTimeFixture.createDefaultReservationTime(),
                        "예약 날짜 및 시간 정보는 비어있을 수 없습니다."
                ),
                Arguments.of(
                        LocalDate.now().plusDays(1),
                        null,
                        ReservationTimeFixture.createDefaultReservationTime(),
                        "테마 정보는 비어있을 수 없습니다."
                ),
                Arguments.of(
                        LocalDate.now().plusDays(1),
                        ThemeFixture.createDefaultTheme(),
                        null,
                        "예약 날짜 및 시간 정보는 비어있을 수 없습니다."
                ),
                Arguments.of(
                        LocalDate.now().minusDays(1),
                        ThemeFixture.createDefaultTheme(),
                        ReservationTimeFixture.createDefaultReservationTime(),
                        "현재보다 이전 시간대로 예약할 수 없습니다."
                ),
                Arguments.of(
                        LocalDate.now(),
                        ThemeFixture.createDefaultTheme(),
                        new ReservationTime(LocalTime.now().minusHours(1)),
                        "현재보다 이전 시간대로 예약할 수 없습니다."
                )
        );
    }

    public static Reservation createDefaultReservationWithName(String name) {
        LocalDate date = LocalDate.now().plusDays(1);
        Theme theme = ThemeFixture.createDefaultTheme();
        ReservationTime time = ReservationTimeFixture.createDefaultReservationTime();
        return Reservation.of(name, date, theme, time);
    }

    public static Reservation createDefaultReservationWithName(String name, Theme theme, ReservationTime time) {
        LocalDate date = LocalDate.now().plusDays(1);
        return createDefaultReservationWithNameAndDate(name, date, theme, time);
    }

    public static Reservation createDefaultReservationWithNameAndDate(String name, LocalDate date, Theme theme,
            ReservationTime time) {
        return Reservation.of(name, date, theme, time);
    }
}
