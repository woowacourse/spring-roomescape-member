package roomescape.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ConflictException;
import roomescape.global.exception.customException.DomainRuleViolationException;
import roomescape.global.exception.customException.NotFoundException;

class ReservationTest {

    private ReservationTime reservationTime;
    private Theme theme;
    private Reservation reservation;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        today = LocalDate.of(2026, 5, 12);
        reservationTime = ReservationTime.createWithId(1L, LocalTime.of(10, 0));
        theme = Theme.createWithId(1L, "테마", "설명", "thumb");
        reservation = Reservation.createWithId(1L, "홍길동", today, reservationTime, theme);
    }

    @Test
    @DisplayName("본인의 예약인지 확인한다.")
    void checkOwnership_success() {
        assertThatCode(() -> reservation.checkOwnership("홍길동"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("본인의 예약이 아닐 경우 예외가 발생한다.")
    void checkOwnership_fail() {
        assertThatThrownBy(() -> reservation.checkOwnership("다른사람"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.RESERVATION_NOT_FOUND_BY_NAME.getMessage());
    }

    @Test
    @DisplayName("동일한 시간이고 ID가 다르면 충돌로 판단한다.")
    void isConflict_true() {
        assertThat(reservation.isConflict(1L, 2L)).isTrue();
    }

    @Test
    @DisplayName("동일한 시간이라도 ID가 같으면 충돌로 판단하지 않는다.")
    void isConflict_false_same_id() {
        assertThat(reservation.isConflict(1L, 1L)).isFalse();
    }

    @Test
    @DisplayName("시간 ID가 다르면 충돌로 판단하지 않는다.")
    void isConflict_false_different_time() {
        assertThat(reservation.isConflict(2L, 2L)).isFalse();
    }

    @Test
    @DisplayName("미래 시간의 예약인지 검증한다.")
    void validateFuture_success() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 12, 9, 0);
        assertThatCode(() -> reservation.validateFuture(now))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("지나간 시간의 예약일 경우 예외가 발생한다.")
    void validateFuture_fail() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 12, 11, 0);
        assertThatThrownBy(() -> reservation.validateFuture(now))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessage(ErrorCode.ILLEGAL_PAST_DATE.getMessage());
    }

    @Test
    @DisplayName("목록 내에 충돌하는 예약이 없으면 검증을 통과한다.")
    void validateUniqueness_success() {
        Reservation other = Reservation.createWithId(2L, "다른사람", today, ReservationTime.createWithId(2L, LocalTime.of(11, 0)), theme);
        assertThatCode(() -> reservation.validateUniqueness(List.of(other)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("목록 내에 충돌하는 예약이 있으면 예외가 발생한다.")
    void validateUniqueness_fail() {
        Reservation other = Reservation.createWithId(2L, "다른사람", today, reservationTime, theme);
        assertThatThrownBy(() -> reservation.validateUniqueness(List.of(other)))
                .isInstanceOf(ConflictException.class)
                .hasMessage(ErrorCode.RESERVATION_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("날짜와 시간을 업데이트한다.")
    void update_success() {
        LocalDate newDate = LocalDate.of(2026, 5, 13);
        ReservationTime newTime = ReservationTime.createWithId(2L, LocalTime.of(11, 0));

        Reservation updated = reservation.update(Optional.of(newDate), Optional.of(newTime));

        assertThat(updated.date()).isEqualTo(newDate);
        assertThat(updated.time()).isEqualTo(newTime);
        assertThat(updated.name()).isEqualTo(reservation.name());
        assertThat(updated.theme()).isEqualTo(reservation.theme());
    }

    @Test
    @DisplayName("업데이트 시 값이 없으면 기존 값을 유지한다.")
    void update_keep_existing() {
        Reservation updated = reservation.update(Optional.empty(), Optional.empty());

        assertThat(updated.date()).isEqualTo(reservation.date());
        assertThat(updated.time()).isEqualTo(reservation.time());
    }
}
