package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import roomescape.exception.RoomEscapeException;

class MemberTest {

    @DisplayName("이름이 null이거나 공백인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void validateInvalidName(String name) {
        assertThatThrownBy(() -> new Member(1L, name, "email@email.com", "1234"))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("사용자 명이 null 이거나 공백으로 이루어질 수 없습니다.");
    }

    @DisplayName("이메일 형식에 맞지 않는 경우 예외가 발생한다.")
    @Test
    void validateInvalidEmail() {
        assertThatThrownBy(() -> new Member(1L, "hotea", "email.com", "1234"))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이메일이 이메일 형식에 맞게 이루어지지 않았습니다.");
    }

}
