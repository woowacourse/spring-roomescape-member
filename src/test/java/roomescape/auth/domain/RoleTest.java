package roomescape.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleTest {

  @DisplayName("관리자 멤버의 관리자 여부로 참을 반환한다.")
  @Test
  void isAdmin() {
    // given
    final Role role = new Role("ADMIN");
    // when
    final boolean isAdmin = role.isAdmin();
    //then
    assertThat(isAdmin).isTrue();
  }

  @DisplayName("일반 멤버의 관리자 여부로 거짓을 반환한다.")
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
