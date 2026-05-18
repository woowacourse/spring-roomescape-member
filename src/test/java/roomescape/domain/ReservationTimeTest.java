package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.DomainException;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간이 null이면 생성에 실패한다.")
    void failCreate_WhenStartAtIsNull() {
        assertThatThrownBy(() -> new ReservationTime(null, null))
                .isInstanceOf(DomainException.class);
    }
}
