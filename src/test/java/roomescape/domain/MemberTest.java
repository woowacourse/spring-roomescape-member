package roomescape.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import roomescape.common.exception.DomainValidationException;

import java.util.stream.Stream;

public class MemberTest {
    @ParameterizedTest
    @ValueSource(strings = {"hippo", "hippo@test", "hippo@.com"})
    @DisplayName("이메일 형식이 아닌 경우 예외 발생 테스트")
    void validEmailFormatTest() {
        Assertions.assertThatThrownBy(() -> new Member(1L, "hippo", "hippo", MemberRole.USER, "123")).isInstanceOf(DomainValidationException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"null", "''", "' '"}, nullValues = "null")
    @DisplayName("이름이 비어있는 경우 예외 발생 테스트")
    void validNameFormatTest(String value) {
        Assertions.assertThatThrownBy(() -> new Member(1L, value, "hippo@test.com", MemberRole.USER, "123")).isInstanceOf(DomainValidationException.class);
    }

    @Test
    @DisplayName("역할이 비어있는 경우 예외 발생 테스트")
    void validMemberRoleRoleTest() {
        Assertions.assertThatThrownBy(() -> new Member(1L, "hippo", "hippo@test.com", null, "123")).isInstanceOf(DomainValidationException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"null", "''", "' '"}, nullValues = "null")
    @DisplayName("패스워드가 비어있는 경우 예외 발생 테스트")
    void validPasswordTest(String value) {
        Assertions.assertThatThrownBy(() -> new Member(1L, "hippo", "hippo", MemberRole.USER, value)).isInstanceOf(DomainValidationException.class);
    }

    @Test
    @DisplayName("입력한 이메일과 비밀번호가 일치하는 경우 : False")
    void checkInvalidLoginFalseTest() {
        //given
        Member member = new Member(1L, "hippo", "hippo@test.com", MemberRole.USER, "123");
        //when & then
        Assertions.assertThat(member.checkInvalidLogin("hippo@test.com","123")).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getEmailAndPassword")
    @DisplayName("입력한 이메일과 비밀번호가 일치하지 않는 경우 : True")
    void checkInvalidLoginTrueTest(String email, String password) {
        //given
        Member member = new Member(1L, "hippo", "hippo@test.com", MemberRole.USER, "123");
        //when & then
        Assertions.assertThat(member.checkInvalidLogin(email,password)).isTrue();
    }

    private static Stream<Arguments> getEmailAndPassword() {
        return Stream.of(
                Arguments.arguments("tippo@test.com","123"),
                Arguments.arguments("hippo@test.com","12")
        );
    }
}
