package roomescape.domain.fixture;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class ReservationFixture {

    public static Stream<Arguments> invalidReservationConstructor() {
        return Stream.of(
                // 날짜 정보가 누락된 경우
                Arguments.of(
                        null,
                        ThemeFixture.createDefaultTheme(),
                        ReservationTimeFixture.createDefaultReservationTime(),
                        "예약 날짜 및 시간 정보는 비어있을 수 없습니다."
                ),
                // 테마 정보가 누락된 경우
                Arguments.of(
                        LocalDate.now().plusDays(1),
                        null,
                        ReservationTimeFixture.createDefaultReservationTime(),
                        "테마 정보는 비어있을 수 없습니다."
                ),
                // 예약 시간 정보가 누락된 경우
                Arguments.of(
                        LocalDate.now().plusDays(1),
                        ThemeFixture.createDefaultTheme(),
                        null,
                        "예약 날짜 및 시간 정보는 비어있을 수 없습니다."
                ),
                // 오늘보다 과거 날짜인 경우
                Arguments.of(
                        LocalDate.now().minusDays(1),
                        ThemeFixture.createDefaultTheme(),
                        ReservationTimeFixture.createDefaultReservationTime(),
                        "현재보다 이전 날짜로 예약할 수 없습니다."
                ),
                // 오늘과 날짜는 동일하지만 시간이 과거인 경우
                Arguments.of(
                        LocalDate.now(),
                        ThemeFixture.createDefaultTheme(),
                        new ReservationTime(LocalTime.now().minusHours(1)),
                        "현재보다 이전 날짜로 예약할 수 없습니다."
                )
        );
    }

    public static Reservation createDefaultReservationWithName(String name) {
        LocalDate date = LocalDate.now().plusDays(1);
        Theme theme = ThemeFixture.createDefaultTheme();
        ReservationTime time = ReservationTimeFixture.createDefaultReservationTime();
        return Reservation.of(name, date, theme, time);
    }
}
