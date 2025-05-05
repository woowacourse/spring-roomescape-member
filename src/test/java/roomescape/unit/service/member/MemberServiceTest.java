package roomescape.unit.service.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.auth.JwtTokenProvider;
import roomescape.domain.member.Member;
import roomescape.dto.member.LoginRequestDto;
import roomescape.dto.member.SignupRequestDto;
import roomescape.service.member.MemberService;
import roomescape.unit.repository.member.FakeMemberRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberServiceTest {

    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private static final String TEST_SECRET_KEY = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(TEST_SECRET_KEY);
        memberService = new MemberService(new FakeMemberRepository(), jwtTokenProvider);
    }

    @Test
    void 회원가입_테스트() {
        long id = memberService.signup(new SignupRequestDto("praisebak", "password"));
        Member memberById = memberService.getMemberById(id);
        assertThat(id).isEqualTo(memberById.getId());
    }

    @Test
    void 로그인시_유저가_존재하면_토큰을_생성한다() {
        memberService.signup(new SignupRequestDto("praisebak", "password"));

        String token = memberService.login(new LoginRequestDto("praisebak", "password"));

        assertThat(token).isNotNull();
    }

    @Test
    void id로_유저를_가져올_수_있다() {
        SignupRequestDto signupRequestDto = new SignupRequestDto("praisebak", "password");
        long id = memberService.signup(signupRequestDto);
        Member memberById = memberService.getMemberById(id);

        assertThat(memberById.getEmail().equals(signupRequestDto.username()));
    }
}
