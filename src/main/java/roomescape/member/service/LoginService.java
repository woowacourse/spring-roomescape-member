package roomescape.member.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.common.exception.LoginException;
import roomescape.common.util.JwtTokenContainer;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.member.dto.LoginCheckResponse;
import roomescape.member.dto.LoginRequest;

@Service
public class LoginService {

    private final JwtTokenContainer jwtTokenContainer;
    private final MemberRepository memberRepository;

    public LoginService(JwtTokenContainer jwtTokenContainer, MemberRepository memberRepository) {
        this.jwtTokenContainer = jwtTokenContainer;
        this.memberRepository = memberRepository;
    }

    public String loginAndReturnToken(LoginRequest request) {
        Optional<Member> loginMember = memberRepository.findByEmailAndPassword(request.email(),
                request.password());
        if (loginMember.isEmpty()) {
            throw new LoginException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenContainer.createJwtToken(loginMember.get());
    }

    public LoginCheckResponse loginCheck(String token) {
        jwtTokenContainer.validateToken(token);
        String name = jwtTokenContainer.getMemberName(token);
        return new LoginCheckResponse(name);
    }
}
