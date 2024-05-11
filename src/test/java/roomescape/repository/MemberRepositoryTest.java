package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.auth.exception.MemberNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(H2MemberRepository.class)
@JdbcTest
class MemberRepositoryTest {

    final List<Member> sampleMembers = List.of(
            new Member(null, "User", "a@b.c", "pw", Role.USER),
            new Member(null, "Admin", "admin@b.c", "pw", Role.ADMIN)
    );

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("아이디를 통해 멤버 정보를 찾는다.")
    void findById() {
        // given
        Member member = sampleMembers.get(0);
        Long id = memberRepository.save(member).getId();

        // when
        Member expected = member.assignId(id);
        Optional<Member> actual = memberRepository.findById(id);

        // then
        assertThat(actual).hasValue(expected);
    }

    @Test
    @DisplayName("이메일을 통해 멤버 정보를 찾는다.")
    void findByEmail() {
        // given
        Member member = sampleMembers.get(0);
        Long id = memberRepository.save(sampleMembers.get(0)).getId();

        // when
        Member expected = member.assignId(id);
        Optional<Member> actual = memberRepository.findByEmail(member.getEmail());

        // then
        assertThat(actual).hasValue(expected);
    }

    @Test
    @DisplayName("멤버가 존재하지 않으면 예외가 발생한다.")
    void exceptionOnFetchByIdIfNotExist() {
        assertThatThrownBy(() -> memberRepository.fetchById(1L))
                .isInstanceOf(MemberNotFoundException.class);
    }
}
