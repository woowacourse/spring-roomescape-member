package roomescape.domain.fixture;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import roomescape.domain.ReservationTime;

public class ReservationFixture {

    public static Stream<Arguments> invalidReservationConstructor() {
        return Stream.of(
                // 날짜 정보가 누락된 경우
                Arguments.of(
                        null,
                        new ReservationTime(LocalTime.of(10, 0)),
                        "예약 날짜 및 시간 정보는 비어있을 수 없습니다."
                ),
                // 예약 시간 정보가 누락된 경우
                Arguments.of(
                        LocalDate.now().plusDays(1),
                        null,
                        "예약 날짜 및 시간 정보는 비어있을 수 없습니다."
                ),
                // 오늘보다 과거 날짜인 경우
                Arguments.of(
                        LocalDate.now().minusDays(1),
                        new ReservationTime(LocalTime.of(10, 0)),
                        "이전 날짜로 예약할 수 없습니다."
                ),
                // 오늘과 날짜는 동일하지만 시간이 과거인 경우
                Arguments.of(
                        LocalDate.now(),
                        new ReservationTime(LocalTime.now().minusHours(1)),
                        "이전 날짜로 예약할 수 없습니다."
                )
        );
    }
}
