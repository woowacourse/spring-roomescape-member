package roomescape.domain.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MemberTest {

    @DisplayName("이메일 형식이 유효하지 않으면 예외가 발생한다")
    @ParameterizedTest
    @MethodSource("provideInvalidEmails")
    void validateEmail(String invalidEmail) {
        assertThatThrownBy(() ->
                new Member(1L, "홍길동", invalidEmail, "password", Role.USER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
    }

    static Stream<String> provideInvalidEmails() {
        return Stream.of(
                "plainaddress",
                "missing@domain",
                "missing.domain@",
                "missing@.com",
                "@missingusername.com",
                "invalid@domain,com"
        );
    }

    @DisplayName("동일한 ID를 가진 경우 equals/hashCode가 동일하게 작동한다")
    @ParameterizedTest
    @MethodSource("provideMembersForEquality")
    void equalsAndHashCode(Member member1, Member member2, boolean expected) {
        // when // then
        assertSoftly(softly -> {
            softly.assertThat(member1.equals(member2)).isEqualTo(expected);
            if (expected) {
                softly.assertThat(member1.hashCode()).isEqualTo(member2.hashCode());
            }
        });
    }

    static Stream<Arguments> provideMembersForEquality() {
        return Stream.of(
                Arguments.of(
                        new Member(1L, "a", "a@a.com", "pw", Role.USER),
                        new Member(1L, "b", "b@b.com", "pw", Role.USER),
                        true
                ),
                Arguments.of(
                        new Member(1L, "a", "a@a.com", "pw", Role.USER),
                        new Member(2L, "a", "a@a.com", "pw", Role.USER),
                        false
                ),
                Arguments.of(
                        new Member(null, "a", "a@a.com", "pw", Role.USER),
                        new Member(null, "a", "a@a.com", "pw", Role.USER),
                        false
                )
        );
    }
}
