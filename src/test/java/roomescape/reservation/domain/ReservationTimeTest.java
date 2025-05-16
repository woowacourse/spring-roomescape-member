package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("시작 시간에 null이 들어가면 예외가 발생한다")
    @Test
    void validate_test() {
        // given
        Long id = 1L;

        // when & then
        assertThatThrownBy(() -> new ReservationTime(id, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("시작 시간은 null일 수 없습니다.");
    }

    @DisplayName("id가 포함된 객체를 반환한다")
    @Test
    void with_id_test() {
        // given
        Long id = 2L;
        LocalTime startAt = LocalTime.now();
        ReservationTime timeWithoutId = new ReservationTime(null, startAt);

        // when
        ReservationTime timeWithId = timeWithoutId.withId(id);

        // then
        assertThat(timeWithId.getId()).isEqualTo(id);
    }

}
