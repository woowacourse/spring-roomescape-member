package roomescape.infrastructure.persistence.dynamic;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationQueryConditionsBuilderTest {

    @Test
    @DisplayName("시작 조건 날짜가 종료 조건 날짜보다 미래인 경우 조건을 만들 수 없다.")
    void periodOrder() {
        ReservationQueryConditionsBuilder builder = ReservationQueryConditions.builder();

        assertThatThrownBy(() -> builder.period("2021-12-25", "2021-12-23"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 조건 날짜는 종료 조건 날짜보다 미래일 수 없습니다.");
    }
}
