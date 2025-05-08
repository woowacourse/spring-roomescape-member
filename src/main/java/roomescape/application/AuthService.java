package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.application.dto.MemberDto;
import roomescape.exception.AuthorizationException;
import roomescape.exception.NotFoundException;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.presentation.controller.dto.MemberResponse;
import roomescape.presentation.controller.dto.TokenRequest;
import roomescape.presentation.controller.dto.TokenResponse;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    private MemberResponse findMember(String principal) {
        Long memberId = Long.parseLong(principal);
        MemberDto memberDto = memberService.getMemberById(memberId);
        return new MemberResponse(memberDto.name());
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        MemberDto memberDto;
        try {
            memberDto = memberService.getMemberBy(tokenRequest.email(), tokenRequest.password());
        } catch (NotFoundException e) {
            throw new AuthorizationException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtTokenProvider.createToken(memberDto);
        return new TokenResponse(accessToken);
    }
}
