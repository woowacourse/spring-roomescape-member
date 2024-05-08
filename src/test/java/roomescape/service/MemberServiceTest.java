package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.dao.MemberDao;
import roomescape.domain.user.Member;
import roomescape.exception.NotExistEmailException;
import roomescape.service.dto.input.MemberCreateInput;
import roomescape.service.dto.input.MemberLoginInput;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService sut;
    @Autowired
    MemberDao memberDao;

    /**
     * 필수 요구사항이 아니므로 임의 구현
     */
    @Test
    @DisplayName("멤버를 생성한다.")
    void create_member() {
        final var input = new MemberCreateInput("조이썬", "sample@naver.com", "password1234");
        final var result = sut.createMember(input);
        assertThatCode(() -> memberDao.findById(result.id())
                .get()).doesNotThrowAnyException();
    }
}
