package roomescape.repository.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Role;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberH2RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원을 추가한다.")
    void saveTest() {
        //given
        Member memberA = new Member(new Name("userA"), new Email("onlytest@test.com"), Role.USER, "password");
        Member memberB = new Member(new Name("userB"), new Email("onlytest@test.com"), Role.USER,"password");

        //when
        Member saveA = memberRepository.save(memberA);
        Member saveB = memberRepository.save(memberB);

        //then
        assertThat(saveB.getId() - saveA.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("회원을 조회한다.")
    void findByEmailTest() {
        //given
        Member memberA = new Member(new Name("userA"), new Email("onlytest@test.com"), Role.USER, "password");

        //when
        Member saveA = memberRepository.save(memberA);
        Optional<Member> findMember = memberRepository.findByEmail(memberA.getEmail());

        //then
        assertThat(findMember.get().getEmail()).isEqualTo(saveA.getEmail());
    }
}