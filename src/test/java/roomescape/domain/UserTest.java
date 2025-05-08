package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("비밀번호 일치 여부를 알 수 있다.")
    void matchesPassword() {
        var user = new User(1L, "포포", UserRole.USER, "popo@email.com", "password");
        assertAll(
            () -> assertThat(user.matchesPassword("password")).isTrue(),
            () -> assertThat(user.matchesPassword("PASSWORD")).isFalse()
        );
    }
}
