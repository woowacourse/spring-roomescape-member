package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import roomescape.domain.Member;
import roomescape.repository.DatabaseCleanupListener;
import roomescape.repository.JdbcMemberRepository;
import roomescape.service.dto.CreateMemberRequest;
import roomescape.service.dto.LoginMemberRequest;
import roomescape.service.exception.MemberNotFoundException;

@TestExecutionListeners(value = {
        DatabaseCleanupListener.class,
        DependencyInjectionTestExecutionListener.class
})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberServiceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JdbcMemberRepository memberRepository;

    private final Member member1 = new Member(null, "t1@t1.com", "123", "러너덕", "MEMBER");
    private final Member member2 = new Member(null, "t2@t2.com", "124", "재즈", "MEMBER");
    private final Member member3 = new Member(null, "t3@t3.com", "125", "재즈덕", "MEMBER");

    @DisplayName("이메일이 중복인 회원을 생성하면 에러를 발생시킨다.")
    @Test
    void throw_exception_when_create_duplicated_member_email() {
        memberRepository.insertMember(member1);

        CreateMemberRequest requestDto = new CreateMemberRequest("t1@t1.com", "11", "워니");

        assertThatThrownBy(() -> memberService.signup(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 가입되어 있는 이메일 주소입니다.");
    }

    @DisplayName("회원을 정상적으로 생성한다.")
    @Test
    void success_signup_member() {
        CreateMemberRequest requestDto = new CreateMemberRequest("t1@t1.com", "11", "워니");

        assertThatNoException()
                .isThrownBy(() -> memberService.signup(requestDto));
    }

    @DisplayName("로그인 시 저장되어있지 않은 이메일이면 에러를 발생시킨다.")
    @Test
    void throw_exception_when_login_not_saved__member_email() {
        LoginMemberRequest requestDto = new LoginMemberRequest("t4@t4.com", "1212");

        assertThatThrownBy(() -> memberService.login(requestDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("회원 정보가 존재하지 않습니다.");
    }

    @DisplayName("로그인 시 비밀번호가 일치하지 않으면 에러를 발생시킨다.")
    @Test
    void throw_exception_when_is_mismatched_password() {
        memberRepository.insertMember(member3);

        LoginMemberRequest requestDto = new LoginMemberRequest("t3@t3.com", "1212");

        assertThatThrownBy(() -> memberService.login(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("로그인이 정상적으로 완료되고 토큰을 발급한다.")
    @Test
    void success_login() {
        Member savedMember = memberRepository.insertMember(member3);
        String expectedToken = jwtService.generateToken(savedMember);
        LoginMemberRequest requestDto = new LoginMemberRequest("t3@t3.com", "125");

        String actualToken = memberService.login(requestDto);

        assertThat(actualToken).isEqualTo(expectedToken);
    }

    @DisplayName("존재하지 않는 멤버 아이디로 조회할 시 에러를 발생시킨다..")
    @Test
    void throw_exception_when_not_found_member_by_id() {
        memberRepository.insertMember(member1);
        Member invalidMember = new Member(2L, "t4@t4.com", "121", "히히", "MEMBER");

        assertThatThrownBy(() -> memberService.findMemberById(3L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("회원 정보가 존재하지 않습니다.");
    }
}
