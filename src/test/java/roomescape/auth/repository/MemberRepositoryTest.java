package roomescape.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.domain.Member;

@SpringBootTest
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @DisplayName("주어진 이메일로 가입한 유저를 찾는다.")
  @Test
  void findByEmail() {
    // Given
    final String email = "kelly@example.com";
    // When
    final Optional<Member> member = memberRepository.findByEmail(email);
    // Then
    assertThat(member.isPresent()).isTrue();
  }

  @DisplayName("가입한 모든 멤버를 조회한다.")
  @Test
  void findAll() {
    // given
    // when
    final List<Member> members = memberRepository.findAll();
    //then
    assertThat(members).hasSize(16);
  }
}
