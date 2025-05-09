package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.exception.custom.InvalidInputException;

class ReservationTest {

    private static Stream<Arguments> provideInvalidValue() {
        return Stream.of(
                Arguments.of(null,
                        LocalDate.of(2025, 1, 1),
                        new ReservationTime(LocalTime.of(10, 0)),
                        new RoomTheme("theme", "description", "thumbnail")),
                Arguments.of(new Member("test", "test@email.com", "1234", MemberRoleType.MEMBER),
                        null,
                        new ReservationTime(LocalTime.of(10, 0)),
                        new RoomTheme("theme", "description", "thumbnail")),
                Arguments.of(new Member("test", "test@email.com", "1234", MemberRoleType.MEMBER),
                        LocalDate.of(2025, 1, 1),
                        null,
                        new RoomTheme("theme", "description", "thumbnail")),
                Arguments.of(new Member("test", "test@email.com", "1234", MemberRoleType.MEMBER),
                        LocalDate.of(2025, 1, 1),
                        new ReservationTime(LocalTime.of(10, 0)),
                        null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidValue")
    @DisplayName("유효하지 않은 값으로 생성 시, 예외를 던진다")
    void throwExceptionWhenInvalidValue(final Member member,
                                        final LocalDate date,
                                        final ReservationTime reservationTime,
                                        final RoomTheme theme) {
        //when & then
        Assertions.assertThatThrownBy(() -> new Reservation(member, date, reservationTime, theme))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("빈 값이 입력될 수 없습니다");
    }
}
