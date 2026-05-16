package roomescape.domain.reservation.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.custom.BusinessException;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

class ReservationValidatorTest {

    @Nested
    @DisplayName("validateOwner 테스트")
    class ValidateOwnerTest {

        @Test
        @DisplayName("예약자 이름과 요청자 이름이 같으면 예외가 발생하지 않는다.")
        void 성공() {
            Reservation reservation = reservation("브라운", LocalDate.of(2026, 5, 1),
                LocalTime.of(13, 0));

            assertThatCode(() -> ReservationValidator.validateOwner("브라운", reservation))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("예약자 이름과 요청자 이름이 다르면 예외가 발생한다.")
        void 실패() {
            Reservation reservation = reservation("브라운", LocalDate.of(2026, 5, 1),
                LocalTime.of(13, 0));

            assertThatThrownBy(() -> ReservationValidator.validateOwner("다른 이름", reservation))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_FORBIDDEN);
        }
    }

    @Nested
    @DisplayName("validateDateAccessable 테스트")
    class ValidateDateAccessableTest {

        @Test
        @DisplayName("미래 예약이면 예외가 발생하지 않는다.")
        void 성공() {
            Reservation reservation = reservation("브라운", LocalDate.of(2026, 5, 1),
                LocalTime.of(13, 0));

            assertThatCode(
                () -> ReservationValidator.validateDateAccessable(reservation, LocalDateTime.of(2026, 1, 1, 0, 0)))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("지난 예약이면 예외가 발생한다.")
        void 실패() {
            Reservation reservation = Reservation.reconstruct(1L, "브라운",
                LocalDate.of(2025, 12, 31),
                Time.reconstruct(1L, LocalTime.of(13, 0)),
                Theme.reconstruct(1L, "테마 이름", "테마 설명", "imageUrl"));

            assertThatThrownBy(
                () -> ReservationValidator.validateDateAccessable(reservation, LocalDateTime.of(2026, 1, 1, 0, 0)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_ALREADY_PASSED);
        }
    }

    @Nested
    @DisplayName("validateDateTimeChangeable 테스트")
    class ValidateDateTimeChangeableTest {

        @Test
        @DisplayName("변경하려는 날짜와 시간이 미래이면 예외가 발생하지 않는다.")
        void 성공() {
            assertThatCode(() -> ReservationValidator.validateDateTimeChangeable(
                LocalDate.of(2026, 5, 1),
                Time.reconstruct(1L, LocalTime.of(13, 0)),
                LocalDateTime.of(2026, 1, 1, 0, 0)))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("변경하려는 날짜가 과거이면 예외가 발생한다.")
        void 실패1() {
            assertThatThrownBy(() -> ReservationValidator.validateDateTimeChangeable(
                LocalDate.of(2025, 12, 31),
                Time.reconstruct(1L, LocalTime.of(13, 0)),
                LocalDateTime.of(2026, 1, 1, 0, 0))
            )
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_TIME_ALREADY_PASSED);
        }

        @Test
        @DisplayName("변경하려는 날짜가 오늘이고 시간이 과거이면 예외가 발생한다.")
        void 실패2() {
            assertThatThrownBy(() -> ReservationValidator.validateDateTimeChangeable(
                LocalDate.of(2026, 1, 1),
                Time.reconstruct(1L, LocalTime.of(9, 0)),
                LocalDateTime.of(2026, 1, 1, 10, 0))
            )
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_TIME_ALREADY_PASSED);
        }
    }

    private Reservation reservation(String name, LocalDate date, LocalTime startAt) {
        return Reservation.create(name, date,
            Time.reconstruct(1L, startAt),
            Theme.reconstruct(1L, "테마 이름", "테마 설명", "imageUrl"),
            LocalDateTime.of(2026, 1, 1, 0, 0));
    }
}
