package roomescape.infrastructure.authentication;

import java.util.Base64;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.infrastructure.persistence.MemberRepository;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.AuthenticatedProfile;
import roomescape.service.auth.AuthenticationRequest;
import roomescape.service.auth.UnauthorizedException;

@Component
class SimpleAuthService implements AuthService {

    private final MemberRepository memberRepository;

    public SimpleAuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public String authenticate(AuthenticationRequest request) {
        Boolean verified = memberRepository.findByEmail(request.email())
                .map(Member::getPassword)
                .map(memberPassword -> memberPassword.equals(request.password()))
                .orElse(false);

        if (!verified) {
            throw new IllegalArgumentException("올바른 인증 정보를 입력해주세요.");
        }

        return generateToken(request);
    }

    private String generateToken(AuthenticationRequest request) {
        String base = request.email() + ":" + request.password();

        return Base64.getEncoder().encodeToString(base.getBytes());
    }

    @Override
    public AuthenticatedProfile authorize(String token) {
        String decoded = new String(Base64.getDecoder().decode(token));
        String[] decodedStrings = decoded.split(":");
        if (decodedStrings.length < 2) {
            throw new UnauthorizedException();
        }

        Member member = memberRepository
                .findByEmail(decodedStrings[0])
                .orElseThrow(UnauthorizedException::new);

        return new AuthenticatedProfile(member.getName().value());
    }
}
