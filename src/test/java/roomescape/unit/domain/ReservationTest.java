package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTest {

    @ParameterizedTest
    @MethodSource("reservationConstructorArguments")
    void 예약은_공백이거나_NULL_로_이루어질_수_없다(Member member, LocalDate date, ReservationTime reservationTime, Theme theme) {
        assertThatThrownBy(() -> new Reservation(1L, member, date, reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> reservationConstructorArguments() {
        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);
        LocalDate today = LocalDate.now();
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        Theme theme = new Theme(1L, "테마", "설명", "썸네일");

        return Stream.of(
                Arguments.of(null, today, reservationTime, theme),
                Arguments.of(member, null, reservationTime, theme),
                Arguments.of(member, today, null, theme),
                Arguments.of(member, today, reservationTime, null)
        );
    }
}

