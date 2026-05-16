package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.global.exception.reservation.CancelledReservationException;
import roomescape.global.exception.reservation.InvalidReservationException;
import roomescape.global.exception.reservation.SameReservationScheduleException;

class ReservationTest {

    private ReservationTime time;
    private Theme theme;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        time = ReservationTime.createNew(LocalTime.of(10, 0));
        theme = Theme.createNew("공포의 저택", "무서운 테마입니다.", "/images/themes/test.webp");
        date = LocalDate.now();
    }

    @Test
    @DisplayName("예약을 정상적으로 생성한다.")
    void createReservation() {
        Reservation reservation = Reservation.createNew("Greene", date, time, theme);

        assertThat(reservation.getName()).isEqualTo("Greene");
        assertThat(reservation.getId()).isNull();
    }

    @Test
    @DisplayName("이름이 정확히 20자인 경우 예약이 정상 생성된다. (경계값 테스트)")
    void validateNameLength_BoundaryPass() {
        String exactBoundaryName = "a".repeat(20);

        assertThatCode(() -> Reservation.createNew(exactBoundaryName, date, time, theme))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이름이 20자를 초과하면 예외가 발생한다. (경계값 밖)")
    void validateNameLength_BoundaryFail() {
        String longName = "a".repeat(21);

        assertThatThrownBy(() -> Reservation.createNew(longName, date, time, theme))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessageContaining("20자 이하");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("이름이 null이거나 비어있으면 예외가 발생한다.")
    void validateName_NullOrBlank(String invalidName) {
        assertThatThrownBy(() -> Reservation.createNew(invalidName, date, time, theme))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessageContaining("비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "가"})
    @DisplayName("이름이 2자 미만이면 예외가 발생한다.")
    void validateName_MinLength(String invalidName) {
        assertThatThrownBy(() -> Reservation.createNew(invalidName, date, time, theme))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessageContaining("2자 이상");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "가나"})
    @DisplayName("이름이 정확히 2자인 경우 예약이 정상 생성된다.")
    void validateName_MinLengthBoundaryPass(String validName) {
        assertThatCode(() -> Reservation.createNew(validName, date, time, theme))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이름이 20자를 초과하면 예외가 발생한다.")
    void validateName_MaxLength20() {
        String invalidName = "a".repeat(21);

        assertThatThrownBy(() -> Reservation.createNew(invalidName, date, time, theme))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessageContaining("20자 이하");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Brown1", "브라운!", "Brown_Lee"})
    @DisplayName("이름에 완성형 한글, 영문, 공백 외 문자가 포함되면 예외가 발생한다.")
    void validateName_AllowedCharacters(String invalidName) {
        assertThatThrownBy(() -> Reservation.createNew(invalidName, date, time, theme))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessageContaining("완성형 한글, 영문, 공백");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Brown", "브라운", "Brown Lee", "브라운 리"})
    @DisplayName("이름이 완성형 한글, 영문, 공백으로만 이루어지면 예약이 정상 생성된다.")
    void validateName_AllowedCharactersPass(String validName) {
        assertThatCode(() -> Reservation.createNew(validName, date, time, theme))
                .doesNotThrowAnyException();
    }

    @Nested
    @DisplayName("필수값 검증")
    class RequiredFields {

        @Test
        @DisplayName("예약 날짜가 null이면 예외가 발생한다.")
        void validateDate_NotNull() {
            assertThatThrownBy(() -> Reservation.createNew("Brown", null, time, theme))
                    .isInstanceOf(InvalidReservationException.class)
                    .hasMessageContaining("예약 날짜");
        }

        @Test
        @DisplayName("예약 시간이 null이면 예외가 발생한다.")
        void validateTime_NotNull() {
            assertThatThrownBy(() -> Reservation.createNew("Brown", date, null, theme))
                    .isInstanceOf(InvalidReservationException.class)
                    .hasMessageContaining("예약 날짜");
        }

        @Test
        @DisplayName("예약 테마가 null이면 예외가 발생한다.")
        void validateTheme_NotNull() {
            assertThatThrownBy(() -> Reservation.createNew("Brown", date, time, null))
                    .isInstanceOf(InvalidReservationException.class)
                    .hasMessageContaining("예약 날짜");
        }
    }

    @Nested
    @DisplayName("일정 변경 검증")
    class ScheduleChange {

        @Test
        @DisplayName("이미 같은 일정이면 변경할 수 없다.")
        void cannotChangeToSameSchedule() {
            Reservation reservation = Reservation.createNew("Brown", date, time, theme);

            assertThatThrownBy(() -> reservation.changeSchedule(date, time))
                    .isInstanceOf(SameReservationScheduleException.class)
                    .hasMessage("이미 같은 일정으로 예약되어 있습니다.");
        }
    }

    @Nested
    @DisplayName("취소 상태 검증")
    class CancelledStatus {

        @Test
        @DisplayName("이미 취소된 예약은 변경할 수 없다.")
        void cannotChangeCancelledReservation() {
            Reservation reservation = Reservation.from(
                    1L,
                    "Brown",
                    date,
                    time,
                    theme,
                    ReservationStatus.CANCELLED
            );

            assertThatThrownBy(() -> reservation.changeSchedule(date.plusDays(1), time))
                    .isInstanceOf(CancelledReservationException.class)
                    .hasMessage("이미 취소된 예약입니다.");
        }

        @Test
        @DisplayName("이미 취소된 예약은 다시 취소할 수 없다.")
        void cannotCancelCancelledReservationAgain() {
            Reservation reservation = Reservation.from(
                    1L,
                    "Brown",
                    date,
                    time,
                    theme,
                    ReservationStatus.CANCELLED
            );

            assertThatThrownBy(reservation::cancel)
                    .isInstanceOf(CancelledReservationException.class)
                    .hasMessage("이미 취소된 예약입니다.");
        }
    }
}
