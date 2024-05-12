package roomescape.auth.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Name;

class MemberTest {

  @DisplayName("어드민 권한 여부를 반환한다 - 참.")
  @Test
  void isAdmin() {
    // given
    Member member = new Member(
        1L,
        new Name("네오"),
        "neo@example.com",
        "password123",
        new Role("ADMIN")
    );
    // when
    boolean isAdmin = member.isAdmin();
    // then
    Assertions.assertThat(isAdmin).isTrue();
  }

  @DisplayName("어드민 권한 여부를 반환한다 - 거짓.")
  @Test
  void isNotAdmin() {
    // given
    Member member = new Member(
        2L,
        new Name("켈리"),
        "kelly@example.com",
        "password123",
        new Role("USER")
    );
    // when
    boolean isAdmin = member.isAdmin();
    // then
    Assertions.assertThat(isAdmin).isFalse();
  }
}
