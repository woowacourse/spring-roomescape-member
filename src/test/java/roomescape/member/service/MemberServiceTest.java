package roomescape.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @DisplayName("존재하는 모든 사용자를 조회한다.")
    @Test
    void findAll() {
        //given
        memberRepository.save(new Member("lini", "lini@email.com", "lini123", Role.GUEST));
        memberRepository.save(new Member("lini2", "lini2@email.com", "lini123", Role.GUEST));
        memberRepository.save(new Member("lini3", "lini3@email.com", "lini123", Role.GUEST));

        //when
        List<MemberResponse> memberResponses = memberService.findAll();

        //then
        assertThat(memberResponses).hasSize(3);
    }

}
