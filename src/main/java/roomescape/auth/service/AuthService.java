package roomescape.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.auth.service.dto.LoginCheckResponse;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.auth.service.dto.LoginResponse;
import roomescape.member.service.dto.SignUpRequest;
import roomescape.exception.InvalidMemberException;
import roomescape.exception.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;

@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Autowired
    public AuthService(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.getByEmail(loginRequest.email());
        validatePassword(loginRequest, member);
        String token = tokenProvider.create(member);
        return new LoginResponse(token);
    }

    private void validatePassword(LoginRequest request, Member member) {
        if (!member.getPassword().equals(request.password())) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
    }

    public LoginCheckResponse check(String token) {
        String email = tokenProvider.extractMemberEmail(token);
        Member member = memberRepository.getByEmail(email);
        return new LoginCheckResponse(member);
    }
}
