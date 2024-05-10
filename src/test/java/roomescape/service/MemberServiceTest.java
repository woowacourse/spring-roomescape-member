package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.dto.SignupRequest;
import roomescape.exception.SignupFailException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberServiceTest {
    private final MemberService service;

    @Autowired
    public MemberServiceTest(final MemberService service) {
        this.service = service;
    }

    private long getMemberSize() {
        return service.findEntireMembers().getData().size();
    }

    @Test
    @DisplayName("사용자 목록을 반환한다.")
    void given_when_findEntireMembers_then_returnMemberResponses() {
        //when, then
        assertThat(service.findEntireMembers().getData().size()).isEqualTo(1);
    }

    @DisplayName("회원정보가 등록되어 있지않으면 회원가입을 성공한다.")
    @Test
    void given_signupRequest_when_createUser_then_success() {
        //given
        SignupRequest signupRequest = new SignupRequest("ash@test.com", "123456", "ash");
        //when
        service.createUser(signupRequest);
        //then
        assertThat(getMemberSize()).isEqualTo(2);
    }

    @DisplayName("이미 회원으로 등록되어 있으면 회원을 가입하지 않는다.")
    @Test
    void given_signupRequest_when_createUserWithAlreadyExistMember_then_fail() {
        //given
        SignupRequest signupRequest = new SignupRequest("poke@test.com", "123456", "ash");
        //when, then
        assertThatThrownBy(() -> service.createUser(signupRequest)).isInstanceOf(SignupFailException.class);
        assertThat(getMemberSize()).isEqualTo(1);
    }
}