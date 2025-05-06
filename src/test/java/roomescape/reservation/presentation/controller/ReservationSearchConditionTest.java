package roomescape.reservation.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationSearchConditionTest {

    @DisplayName("조건이 모두 null이면 비어있는 조건이다.")
    @Test
    void emptyCondition_whenAllNull() {
        // given
        ReservationSearchCondition condition = new ReservationSearchCondition(null, null, null, null);

        // when
        boolean empty = condition.isEmpty();

        // then
        assertThat(empty).isTrue();
    }

    @DisplayName("조건이 하나라도 존재한다면 조건은 비어있지 않다.")
    @Test
    void emptyCondition_whenAtLeastOne() {
        // given
        ReservationSearchCondition condition = new ReservationSearchCondition(1L, null, null, null);

        // when
        boolean empty = condition.isEmpty();

        // then
        assertThat(empty).isFalse();
    }


}
