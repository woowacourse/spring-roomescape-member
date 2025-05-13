package roomescape.member.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.common.exception.LoginException;
import roomescape.common.util.DateTime;
import roomescape.common.util.JwtTokenManager;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.member.dto.request.LoginMember;
import roomescape.member.dto.request.LoginRequest;

@Service
public class LoginService {

    private final JwtTokenManager jwtTokenManager;
    private final MemberRepository memberRepository;
    private final DateTime dateTime;

    public LoginService(JwtTokenManager jwtTokenManager, MemberRepository memberRepository, DateTime dateTime) {
        this.jwtTokenManager = jwtTokenManager;
        this.memberRepository = memberRepository;
        this.dateTime = dateTime;
    }

    public String loginAndReturnToken(LoginRequest request) {
        Optional<Member> loginMember = memberRepository.findByEmailAndPassword(request.email(),
                request.password());
        if (loginMember.isEmpty()) {
            throw new LoginException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenManager.createJwtToken(loginMember.get(), dateTime.now());
    }

    public LoginMember loginCheck(String token) {
        Long memberId = jwtTokenManager.validateTokenAndGetMemberId(token);
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new LoginException("유효하지 않은 회원입니다.");
        }
        Member findMember = member.get();
        return new LoginMember(memberId, findMember.getName());
    }
}
