package roomescape.time.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.BusinessRuleViolationException;
import roomescape.exception.InvalidDomainStateException;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    @DisplayName("정상적인 시작 시간을 입력받으면 객체가 생성된다.")
    void createSuccess() {
        // given
        LocalTime startAt = LocalTime.of(13, 0);

        // when
        ReservationTime reservationTime = new ReservationTime(1L, startAt);

        // then
        assertThat(reservationTime.getId()).isEqualTo(1L);
        assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    @DisplayName("정적 팩토리 메서드 create를 통해 ID가 없는 객체를 생성할 수 있다.")
    void createWithStaticMethod() {
        // given
        LocalTime startAt = LocalTime.of(15, 30);

        // when
        ReservationTime reservationTime = ReservationTime.fromValidTimeUnit(startAt);

        // then
        assertThat(reservationTime.getId()).isNull();
        assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    @DisplayName("시작 시간이 null이면 예외를 발생한다.")
    void failWhenStartAtIsNull() {
        assertThatThrownBy(() -> new ReservationTime(1L, null))
                .isInstanceOf(InvalidDomainStateException.class);
    }


    @Test
    @DisplayName("예약을 30분 단위로 추가하지 않으면 예외가 발생한다.")
    void validTimeUnit() {
        // given
        LocalTime inValidStartAt = LocalTime.of(12, 24);

        // when & then
        assertThatThrownBy(() -> ReservationTime.fromValidTimeUnit(inValidStartAt))
                .isInstanceOf(BusinessRuleViolationException.class);
    }
}
