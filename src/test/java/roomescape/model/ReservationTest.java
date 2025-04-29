package roomescape.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ReservationTest {

    public static Stream<Arguments> NullValues() {
        return Stream.of(
            Arguments.of(
                null,
                LocalDate.of(2023, 12, 1),
                new TimeSlot(1L, LocalTime.of(10, 0))
            ),
            Arguments.of(
                "brown",
                null,
                new TimeSlot(1L, LocalTime.of(10, 0))
            ),
            Arguments.of(
                "brown",
                LocalDate.of(2023, 12, 1),
                null
            )
        );
    }

    @ParameterizedTest
    @MethodSource("NullValues")
    @DisplayName("예약 생성 시 id가 아닌 모든 값들이 존재하지 않으면 예외가 발생한다")
    void ExceptionAnyValueNull(String name, LocalDate date, TimeSlot timeSlot) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Reservation(1L, name, date, timeSlot))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이름이 여섯 글자 이상이면 예외가 발생한다")
    void ExceptionNameLength() {
        // given
        // when

        // then
        assertThatThrownBy(() -> new Reservation(
            1L,
            "여섯글자이름",
            LocalDate.of(2023, 12, 1),
            new TimeSlot(1L, LocalTime.of(10, 0)))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
