package roomescape.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleTest {

  @DisplayName("어드민 권한 여부를 반환한다.")
  @Test
  void isAdmin() {
    // given
    final Role role = new Role("ADMIN");
    // when
    final boolean isAdmin = role.isAdmin();
    //then
    assertThat(isAdmin).isTrue();
  }

  @DisplayName("어드민 권한 여부를 반환한다.")
  @Test
  void isNotAdmin() {
    // given
    final Role role = new Role("USER");
    // when
    final boolean isAdmin = role.isAdmin();
    //then
    assertThat(isAdmin).isFalse();
  }
}
