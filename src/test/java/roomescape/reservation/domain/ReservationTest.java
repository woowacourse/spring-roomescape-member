package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;

class ReservationTest {

    @DisplayName("id가 포함된 객체를 반환한다")
    @Test
    void with_id_test() {
        // given
        Theme theme = sampleTheme();
        ReservationTime time = sampleTime();
        Member member = sampleMember();
        Reservation emptyIdReservation = new Reservation(null, LocalDate.now(), time, theme, member);

        Long reservationId = 3L;

        // when
        Reservation reservationWithId = emptyIdReservation.withId(reservationId);

        // then
        assertThat(reservationWithId.getId()).isEqualTo(reservationId);
    }

    @DisplayName("현재 날짜와 동일하거나 이후인지 여부를 반환한다")
    @MethodSource
    @ParameterizedTest
    void is_same_or_after_test(LocalDate from, boolean expected) {
        // given
        Theme theme = sampleTheme();
        ReservationTime time = sampleTime();
        Member member = sampleMember();
        LocalDate now = LocalDate.now();

        Reservation reservation = new Reservation(1L, now, time, theme, member);

        // when & then
        assertThat(reservation.isSameOrAfter(from)).isEqualTo(expected);
    }

    @DisplayName("현재 날짜와 동일하거나 이전인지 여부를 반환한다")
    @MethodSource
    @ParameterizedTest
    void is_same_or_before_test(LocalDate from, boolean expected) {
        // given
        Theme theme = sampleTheme();
        ReservationTime time = sampleTime();
        Member member = sampleMember();
        LocalDate now = LocalDate.now();

        Reservation reservation = new Reservation(1L, now, time, theme, member);

        // when & then
        assertThat(reservation.isSameOrBefore(from)).isEqualTo(expected);
    }

    @DisplayName("필수 값에 null이 들어가면 예외가 발생한다")
    @MethodSource
    @ParameterizedTest
    void validate_test(LocalDate date, ReservationTime time, Theme theme, Member member, String message) {
        // given
        Long id = 1L;

        // when & then
        assertThatThrownBy(() -> new Reservation(id, date, time, theme, member))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(message);
    }

    static Stream<Arguments> is_same_or_after_test() {
        return Stream.of(
                Arguments.of(
                        LocalDate.now(),
                        true
                ),
                Arguments.of(
                        LocalDate.now().plusDays(3),
                        false
                ),
                Arguments.of(
                        LocalDate.now().minusDays(1),
                        true
                )
        );
    }

    static Stream<Arguments> is_same_or_before_test() {
        return Stream.of(
                Arguments.of(
                        LocalDate.now(),
                        true
                ),
                Arguments.of(
                        LocalDate.now().plusDays(3),
                        true
                ),
                Arguments.of(
                        LocalDate.now().minusDays(1),
                        false
                )
        );
    }

    static Stream<Arguments> validate_test() {
        return Stream.of(
                Arguments.of(null, sampleTime(), sampleTheme(), sampleMember(), "예약 날짜는 null일 수 없습니다."),
                Arguments.of(LocalDate.now(), null, sampleTheme(), sampleMember(), "예약 시간은 null일 수 없습니다."),
                Arguments.of(LocalDate.now(), sampleTime(), null, sampleMember(), "테마는 null일 수 없습니다."),
                Arguments.of(LocalDate.now(), sampleTime(), sampleTheme(), null, "사용자는 null일 수 없습니다.")
        );
    }

    static Theme sampleTheme() {
        return new Theme(1L, "테마", "테마", "theme");
    }

    static ReservationTime sampleTime() {
        return new ReservationTime(1L, LocalTime.now());
    }

    static Member sampleMember() {
        return new Member(1L, "루키", "rookie@email.com", "rookie123", Role.USER);
    }

}
