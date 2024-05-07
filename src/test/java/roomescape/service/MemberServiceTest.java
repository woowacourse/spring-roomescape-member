package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberPassword;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.MemberSignupRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:test_db_clean.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class MemberServiceTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("사용자를 추가한다.")
    void createMember() {
        //given
        String name = "abc";
        String email = "test@test.com";
        String password = "1234";
        MemberSignupRequest request = new MemberSignupRequest(name, email, password);

        //when
        MemberResponse result = memberService.add(request);

        //then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(name),
                () -> assertThat(result.getEmail()).isEqualTo(email)
        );
    }

    @Test
    @DisplayName("이메일이 저장소에 존재하면 예외가 발생한다.")
    void createMemberWithExistEmail() {
        //given
        String email = "test@test.com";
        memberDao.create(createMember("daon", email, "1432"));
        MemberSignupRequest request = new MemberSignupRequest("abc", email, "1234");

        //when //then
        assertThatThrownBy(() -> memberService.add(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Member createMember(String name, String email, String password) {
        return new Member(null, new MemberName(name), new MemberEmail(email), new MemberPassword(password));
    }
}
