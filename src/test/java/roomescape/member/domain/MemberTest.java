package roomescape.member.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.ReservationException;

public class MemberTest {

    @Test
    void 이름이_null이면_예외가_발생한다() {
        // given
        final String name = null;

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Member(1L, name, "wooga@email.com", "1234", Role.USER);
        }).isInstanceOf(ReservationException.class);
    }

    @Test
    void 이름이_빈_문자열이면_예외가_발생한다() {
        // given
        final String name = "";

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Member(1L, name, "wooga@email.com", "1234", Role.USER);
        }).isInstanceOf(ReservationException.class);
    }
}
