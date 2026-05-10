package roomescape.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberNameTest {

    @Test
    void 유효한_이름으로_생성할_수_있다() {
        // given & when
        MemberName name = new MemberName("코로구");

        // then
        assertThat(name.value()).isEqualTo("코로구");
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
