package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberNameTest {

    @ParameterizedTest
    @DisplayName("예약자명이 1글자 이상, 20글자 이하로 입력되면 에러가 발생한다.")
    @ValueSource(strings = {"", " ", "aaaaaaaaaaaaaaaaaaaaa"})
    void checkMemberNameLength(String memberName) {
        //given & when & then
        assertThatThrownBy(() -> new MemberName(memberName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자명은 1글자 이상, 20글자 이하로 작성해야 합니다.");
    }

    @ParameterizedTest
    @DisplayName("예약자명에 한글, 영문, 숫자 외의 문자가 포함되면 에러가 발생한다.")
    @ValueSource(strings = {"chorong!", "ma_son", "cho rong"})
    void checkMemberNameFormat(String memberName) {
        //given & when & then
        assertThatThrownBy(() -> new MemberName(memberName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자명은 한글, 영문, 숫자만 허용합니다.");
    }
}
