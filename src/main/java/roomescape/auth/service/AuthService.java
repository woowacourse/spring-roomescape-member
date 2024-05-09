package roomescape.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.controller.dto.LoginRequest;
import roomescape.auth.controller.dto.SignUpRequest;
import roomescape.auth.controller.dto.TokenResponse;
import roomescape.auth.domain.AuthInfo;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorType;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberSignUp;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public void authenticate(LoginRequest loginRequest) {
        if (!memberRepository.existsBy(loginRequest.email(), loginRequest.password())) {
            throw new BusinessException(ErrorType.MEMBER_NOT_FOUND);
        }
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        return new TokenResponse(tokenProvider.createAccessToken(loginRequest.email()));
    }

    public AuthInfo fetchByToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new BusinessException(ErrorType.INVALID_TOKEN);
        }
        Member member = memberRepository.findBy(tokenProvider.getPayload(token))
                .orElseThrow(() -> new BusinessException(ErrorType.MEMBER_NOT_FOUND));
        return AuthInfo.of(member);
    }

    public void signUp(SignUpRequest signUpRequest) {
        memberRepository.save(
                new MemberSignUp(signUpRequest.name(), signUpRequest.email(), signUpRequest.password(), Role.USER));
    }
}
