package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import roomescape.BasicAcceptanceTest;
import roomescape.dto.request.UserLoginRequest;
import roomescape.dto.request.UserSignUpRequest;

@Sql("/setMember.sql")
class MemberServiceTest extends BasicAcceptanceTest {
    @Autowired
    private MemberService memberService;

    @DisplayName("중복된 이메일로 회원가입을 시도할 시 예외를 발생시킨다")
    @Test
    void rejectDuplicateMember() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("name", "email1", "pass");

        assertThatThrownBy(() -> memberService.save(userSignUpRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 이메일의 계정이 존재합니다. 입력한 이메일: " + userSignUpRequest.email());
    }

    @DisplayName("이메일과 패스워드 모두 일치하는 계정이 없을 시 예외를 발생시킨다")
    @Test
    void notExistEmailAndPassword() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("email0", "qw");

        assertThatThrownBy(() -> memberService.createToken(userLoginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가입되어 있지 않은 유저입니다.");
    }

    @DisplayName("id가 일치하는 계정이 없을 시 예외를 발생시킨다")
    @Test
    void notExistId() {
        assertThatThrownBy(() -> memberService.findById(3L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가입되어 있지 않은 유저입니다.");
    }
}
