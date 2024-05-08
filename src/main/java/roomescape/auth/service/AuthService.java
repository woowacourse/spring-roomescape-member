package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.controller.dto.LoginRequest;
import roomescape.auth.controller.dto.SignUpRequest;
import roomescape.auth.controller.dto.TokenResponse;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorType;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberSignUp;
import roomescape.member.domain.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public void authenticate(LoginRequest loginRequest) {
        if (!memberRepository.existsBy(loginRequest.email(), loginRequest.password())) {
            throw new BusinessException(ErrorType.MEMBER_NOT_FOUND);
        }
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        return new TokenResponse(tokenProvider.createAccessToken(loginRequest.email()));
    }

    public MemberResponse fetchByToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new BusinessException(ErrorType.INVALID_TOKEN);
        }
        Member member = memberRepository.findBy(tokenProvider.getPayload(token))
                .orElseThrow(() -> new BusinessException(ErrorType.MEMBER_NOT_FOUND));
        return MemberResponse.from(member);
    }

    public void signUp(SignUpRequest signUpRequest) {
        memberRepository.save(new MemberSignUp(signUpRequest.name(), signUpRequest.email(), signUpRequest.password()));
    }
}
