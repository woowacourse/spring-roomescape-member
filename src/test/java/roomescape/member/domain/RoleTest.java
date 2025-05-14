package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleTest {
    @DisplayName("이름에 맞는 역할을 반환한다")
    @Test
    void findByName() {
        // given
        String user = "user";
        String admin = "admin";

        // when
        Role userRole = Role.findBy(user);
        Role adminRole = Role.findBy(admin);

        // then
        assertThat(userRole).isEqualTo(Role.USER);
        assertThat(adminRole).isEqualTo(Role.ADMIN);
    }
}
