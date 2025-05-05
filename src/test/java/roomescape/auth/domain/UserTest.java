package roomescape.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @DisplayName("식별자는 email이며, email이 같으면 같은 user이다.")
    @Test
    void identify_withEmail() {
        // given
        String email = "test@email.com";

        // when
        User user1 = new User(email, "password", "멍구");
        User user2 = new User(email, "password2", "멍구2");

        // then
        assertThat(user1).isEqualTo(user2);
    }

}
