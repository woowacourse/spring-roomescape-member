package roomescape.auth.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleTest {

  @DisplayName("어드민 권한 여부를 반환한다.")
  @Test
  void isAdmin() {
    // given
    Role role = new Role("ADMIN");
    // when
    boolean isAdmin = role.isAdmin();
    //then
    Assertions.assertThat(isAdmin).isTrue();
  }

  @DisplayName("어드민 권한 여부를 반환한다.")
  @Test
  void isNotAdmin() {
    // given
    Role role = new Role("USER");
    // when
    boolean isAdmin = role.isAdmin();
    //then
    Assertions.assertThat(isAdmin).isFalse();
  }
}
