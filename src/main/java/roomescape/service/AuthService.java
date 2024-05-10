package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.LoginCheckResponse;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.domain.infrastucture.JwtTokenProvider;
import roomescape.domain.repository.MemberRepository;
import roomescape.dto.request.LoginCheckRequest;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginMember;
import roomescape.dto.response.TokenResponse;
import roomescape.exception.AuthenticationException;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        Member member = memberRepository
                .findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        String token = jwtTokenProvider.createToken(member);
        return new TokenResponse(token);
    }

    public LoginCheckResponse checkLogin(LoginCheckRequest loginCheckRequest) {
        String token = loginCheckRequest.token();
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException("권한이 없습니다.");
        }

        String memberName = jwtTokenProvider.getMemberNameFrom(token);

        return new LoginCheckResponse(memberName);
    }

    public LoginMember getLoginMember(LoginMemberRequest loginMemberRequest) {
        String token = loginMemberRequest.token();
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException("권한이 없습니다.");
        }

        Long memberId = jwtTokenProvider.getMemberIdFrom(token);
        String memberEmail = jwtTokenProvider.getMemberEmailFrom(token);
        String memberName = jwtTokenProvider.getMemberNameFrom(token);
        Role memberRole = jwtTokenProvider.getMemberRoleFrom(token);

        return new LoginMember(memberId, memberEmail, memberName, memberRole);
    }
}
