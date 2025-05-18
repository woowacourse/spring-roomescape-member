package roomescape.domain.entity;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReservationTest {

    @DisplayName("동일한 ID를 가진 경우 equals/hashCode가 동일하게 작동한다")
    @ParameterizedTest
    @MethodSource("provideReservationsForEquality")
    void equalsAndHashCode(Reservation r1, Reservation r2, boolean expected) {
        assertSoftly(soft -> {
            soft.assertThat(r1.equals(r2)).isEqualTo(expected);
            if (expected) {
                soft.assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
            }
        });
    }

    static Stream<Arguments> provideReservationsForEquality() {
        var member = new Member(1L, "홍길동", "a@a.com", "pw", Role.USER);
        var time = new ReservationTime(1L, LocalTime.of(10, 0));
        var theme = new Theme(1L, "이름", "설명", "썸네일");
        return Stream.of(
                Arguments.of(new Reservation(1L, member, LocalDate.now(), time, theme),
                        new Reservation(1L, member, LocalDate.now(), time, theme), true),
                Arguments.of(new Reservation(1L, member, LocalDate.now(), time, theme),
                        new Reservation(2L, member, LocalDate.now(), time, theme), false),
                Arguments.of(new Reservation(null, member, LocalDate.now(), time, theme),
                        new Reservation(null, member, LocalDate.now(), time, theme), false)
        );
    }
}
