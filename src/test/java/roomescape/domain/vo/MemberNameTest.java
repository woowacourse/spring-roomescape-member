package roomescape.domain.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberNameTest {

    @Test
    void 같은_이름_동일_객체_테스트() {
        //given
        MemberName memberName1 = new MemberName("제임스");
        MemberName memberName2 = new MemberName("제임스");

        // when, then
        assertThat(memberName1).isEqualTo(memberName2);
    }

    @Test
    void 다른_이름_다른_객체_테스트() {
        //given
        MemberName memberName1 = new MemberName("제임스");
        MemberName memberName2 = new MemberName("포비");

        // when, then
        assertThat(memberName1).isNotEqualTo(memberName2);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "\t\n"})
    void 빈_문자열로_생성하면_예외가_발생한다(String value) {
        // when & then
        assertThatThrownBy(() -> new MemberName(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 문자열은");
    }
}