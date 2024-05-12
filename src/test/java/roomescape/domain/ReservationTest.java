package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.fixture.MemberFixture;
import roomescape.fixture.ThemeFixture;

public class ReservationTest {

    @Test
    @DisplayName("id, name, reservationDate, reservationTime 을 통해 도메인을 생성한다.")
    void create_with_id_name_reservationDate_reservationTime() {
        assertThatCode(() ->
                new Reservation(
                        null,
                        MemberFixture.getDomain("제리"),
                        ReservationDate.from("2024-04-03"),
                        ReservationTime.of(null, "10:00"),
                        ThemeFixture.getDomain("테마 1")
                ))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("id, 문자열들 을 통해 도메인을 생성한다.")
    void create_with_factory_method() {
        assertThatCode(() ->
                Reservation.of(
                        null,
                        MemberFixture.getDomain("jerry"),
                        "2024-04-03",
                        ReservationTime.of(null, "10:00"),
                        ThemeFixture.getDomain("테마 1")))
                .doesNotThrowAnyException();
    }

    private static Stream<Arguments> maskingDateAndTime() {
        return Stream.of(
                Arguments.arguments(Reservation.of(null, MemberFixture.getDomain("jerry"), "2024-04-01",
                        ReservationTime.of(null, "10:00"),
                        ThemeFixture.getDomain("테마 1"))),
                Arguments.arguments(Reservation.of(null, MemberFixture.getDomain("jerry"), "2024-04-02", ReservationTime.of(null, "09:59"),
                        ThemeFixture.getDomain("테마 1")))
        );
    }

    @ParameterizedTest
    @MethodSource("maskingDateAndTime")
    @DisplayName("날짜가 이전이거나 날짜가 같을 때 시간이 이전이면 참을 반환한다.")
    void return_true_when_date_is_before_or_date_is_equal_and_time_is_before(final Reservation reservation) {
        final var result = reservation.isBefore(LocalDate.parse("2024-04-02"), LocalTime.parse("10:00"));
        assertThat(result).isTrue();
    }
}
