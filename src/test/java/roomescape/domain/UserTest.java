package roomescape.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @ParameterizedTest
    @ValueSource(strings = {"correct@email.com", "wrong@email.com"})
    void isEmailMatchesTest(String expectedEmail) {
        String email = "correct@email.com";
        boolean expectedMatches = expectedEmail.equals(email);
        User user = new User("name", email, "password");

        boolean actualMatches = user.isEmailMatches(expectedEmail);

        assertThat(actualMatches).isEqualTo(expectedMatches);
    }

    @ParameterizedTest
    @ValueSource(strings = {"correct", "wrong"})
    void isPasswordMatchesTest(String expectedPassword) {
        String password = "correct";
        boolean expectedMatches = expectedPassword.equals(password);
        User user = new User("name", "email@email.com", password);

        boolean actualMatches = user.isPasswordMatches(expectedPassword);

        assertThat(actualMatches).isEqualTo(expectedMatches);
    }
}
