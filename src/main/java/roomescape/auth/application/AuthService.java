package roomescape.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.exception.NotAuthenticatedException;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(LoginRequest request) {
        Member member = findMemberByEmail(request.email());
        if (!member.hasSamePassword(request.password())) {
            throw new NotAuthenticatedException("사용자의 비밀번호와 입력 비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenProvider.createToken(member.getEmail());
    }

    public Member extractMember(String token) {
        jwtTokenProvider.validateToken(token);
        String email = jwtTokenProvider.getPayload(token);
        return findMemberByEmail(email);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotAuthenticatedException("해당 이메일의 사용자가 없습니다."));
    }
}
