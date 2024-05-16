package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

  @DisplayName("유효하지 않은 길이의 이름이 입력되면 예외를 발생시킨다.")
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"kellykelly"})
  void playerNameLengthTest(final String invalidName) {
    // When & Then
    assertThatThrownBy(() -> new Name(invalidName))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("예약자 이름은 1글자 이상 5글자 이하여야 합니다.");
  }
}
