package roomescape.auth.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

  @DisplayName("어드민 권한 여부를 반환한다 - 참.")
  @Test
  void isAdmin() {
    // given
    final Member member = Member.createInstance(
        1L,
        "네오",
        "neo@example.com",
        "password123",
        "ADMIN"
    );
    // when
    final boolean isAdmin = member.isAdmin();
    // then
    Assertions.assertThat(isAdmin).isTrue();
  }

  @DisplayName("어드민 권한 여부를 반환한다 - 거짓.")
  @Test
  void isNotAdmin() {
    // given
    final Member member = Member.createInstance(
        2L,
        "켈리",
        "kelly@example.com",
        "password123",
        "USER"
    );
    // when
    final boolean isAdmin = member.isAdmin();
    // then
    Assertions.assertThat(isAdmin).isFalse();
  }
}
