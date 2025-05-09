package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.exception.UnauthorizedException;
import roomescape.repository.member.MemberRepository;
import roomescape.utility.JwtTokenProvider;

@Service
public class AuthenticationService {

    private final MemberRepository h2MemberRepository;
    private final JwtTokenProvider jwtTokenManager;

    public AuthenticationService(MemberRepository memberRepository, JwtTokenProvider jwtTokenManager) {
        this.h2MemberRepository = memberRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String login(String email, String password) {
        Member member = loadMemberByEmail(email);
        validateSamePassword(member, password);
        return jwtTokenManager.makeAccessToken(member.getId(), member.getName(), member.getRole());
    }

    private Member loadMemberByEmail(String email) {
        Optional<Member> member = h2MemberRepository.findByEmail(email);
        return member.orElseThrow(
                () -> new UnauthorizedException("[ERROR] 유효하지 않은 인증정보입니다."));
    }

    private void validateSamePassword(Member member, String password) {
        boolean isSamePassword = member.comparePassword(password);
        if (!isSamePassword) {
            throw new UnauthorizedException("[ERROR] 유효하지 않은 인증정보입니다.");
        }
    }
}
