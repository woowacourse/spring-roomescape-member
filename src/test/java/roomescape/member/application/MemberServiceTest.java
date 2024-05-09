package roomescape.member.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.global.exception.NotFoundException;
import roomescape.member.domain.Member;

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
    @DisplayName("Id로 조회하려는 사용자가 존재하지 않는 경우 예외가 발생한다.")
    void findById() {
        // given
        Long notExistingId = 10L;

        // when & then
        assertThatThrownBy(() -> memberService.findById(notExistingId))
                .isInstanceOf(NotFoundException.class);
    }
}
