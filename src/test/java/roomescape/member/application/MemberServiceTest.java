package roomescape.member.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.exception.MemberNotExistException;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.TestFixture.USER_MIA;

@Sql("/test-schema.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberServiceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자가 가입한다.")
    void create() {
        // given
        Member member = USER_MIA();

        // when
        Member createdMember = memberService.create(member);

        // then
        assertThat(createdMember.getId()).isNotNull();
    }

    @Test
    @DisplayName("이메일로 조회하려는 사용자가 존재하지 않는 경우 예외가 발생한다.")
    void findByEmail() {
        // given
        memberRepository.save(USER_MIA());
        String notExistingEmail = "notExistingEmail@google.com";

        // when & then
        assertThatThrownBy(() -> memberService.findByEmail(notExistingEmail))
                .isInstanceOf(MemberNotExistException.class);
    }
}
