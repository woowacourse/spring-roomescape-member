package roomescape.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberTest {

    @ParameterizedTest
    @ValueSource(strings = {"correct@email.com", "wrong@email.com"})
    void isEmailMatchesTest(String expectedEmail) {
        String email = "correct@email.com";
        boolean expectedMatches = expectedEmail.equals(email);
        Member member = new Member(1L, "name", Role.USER, email, "password");

        boolean actualMatches = member.isEmailMatches(expectedEmail);

        assertThat(actualMatches).isEqualTo(expectedMatches);
    }

    @ParameterizedTest
    @ValueSource(strings = {"correct", "wrong"})
    void isPasswordMatchesTest(String expectedPassword) {
        String password = "correct";
        boolean expectedMatches = expectedPassword.equals(password);
        Member member = new Member(1L, "name", Role.USER, "email@email.com", password);

        boolean actualMatches = member.isPasswordMatches(expectedPassword);

        assertThat(actualMatches).isEqualTo(expectedMatches);
    }

    @Test
    void isAdminTest() {
        Member admin = new Member(1L, "admin", Role.ADMIN, "email@email.com", "password");
        Member user = new Member(1L, "user", Role.USER, "email@email.com", "password");

        assertThat(admin.isAdmin()).isTrue();
        assertThat(user.isAdmin()).isFalse();
    }
}
