package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidInputException;

class UserNameTest {

    @ParameterizedTest
    @DisplayName("예약자명에 빈 값이 아닌 값이 입력되었는지 확인한다.")
    @ValueSource(strings = {"", " "})
    void checkUserNameBlank(String userName) {
        //given & when & then
        assertThatThrownBy(() -> new UserName(userName))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약자명이 입력되지 않았습니다. 1글자 이상 20글자 이하로 입력해주세요.");
    }

    @ParameterizedTest
    @DisplayName("예약자명이 1글자 이상, 20글자 이하로 입력되었는지 확인한다.")
    @ValueSource(strings = {"aaaaaaaaaaaaaaaaaaaaa"})
    void checkUserNameLength(String userName) {
        //given & when & then
        assertThatThrownBy(() -> new UserName(userName))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage(userName + "은 유효하지 않은 예약자명입니다. 20글자 이하로 입력해주세요.");
    }

    @ParameterizedTest
    @DisplayName("예약자명에 한글, 영문, 숫자 외의 문자가 포함되었는지 확인한다.")
    @ValueSource(strings = {"chorong!", "ma_son", "cho rong"})
    void checkUserNameFormat(String userName) {
        //given & when & then
        assertThatThrownBy(() -> new UserName(userName))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage(userName + "은 유효하지 않은 예약자명입니다. 한글, 영문, 숫자로만 입력해주세요.");
    }
}
