package roomescape.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.exception.AuthenticationException.InvalidCredentialsException;
import roomescape.auth.infrastructure.TokenProvider;
import roomescape.auth.infrastructure.dto.CredentialDetails;
import roomescape.auth.service.dto.response.CredentialServiceResponse;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public String createAuthenticationToken(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new InvalidCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다."));
        return tokenProvider.create(member);
    }

    public CredentialServiceResponse getCredentialDetails(String token) {
        CredentialDetails credentialDetails = tokenProvider.extractToCredentialDetails(token);
        return CredentialServiceResponse.from(credentialDetails);
    }
}
