package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.AuthInformationResponse;
import roomescape.auth.dto.CreateTokenRequest;
import roomescape.auth.dto.CreateTokenResponse;
import roomescape.common.exception.AuthorizationException;
import roomescape.member.model.Member;
import roomescape.member.repositoy.JdbcMemberRepository;
import roomescape.util.JwtTokenProvider;

@Service
public class AuthService {

    private final JdbcMemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JdbcMemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public CreateTokenResponse createToken(CreateTokenRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(AuthorizationException::new);

        String accessToken = jwtTokenProvider.createToken(member);
        return new CreateTokenResponse(accessToken);
    }

    public AuthInformationResponse findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token, "name");
        return new AuthInformationResponse(payload);
    }
}
