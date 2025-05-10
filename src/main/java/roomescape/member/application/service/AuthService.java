package roomescape.member.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import roomescape.member.InvalidMemberException;
import roomescape.member.application.dto.CreateMemberRequest;
import roomescape.member.application.repository.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.infrastructure.JwtTokenProvider;
import roomescape.member.presentation.dto.LoginRequest;
import roomescape.member.presentation.dto.MemberResponse;
import roomescape.member.presentation.dto.RegisterRequest;
import roomescape.member.presentation.dto.TokenResponse;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new InvalidMemberException("이메일이 올바르지 않습니다.", HttpStatus.NOT_FOUND));
        validateUserLogin(member, loginRequest);
        return tokenProvider.createToken(member);
    }

    public Member getUser(String token) {
        String email = tokenProvider.resolveToken(token);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidMemberException("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND));
    }

    public MemberResponse signup(RegisterRequest registerRequest) {
        CreateMemberRequest request = new CreateMemberRequest(
                registerRequest.name(),
                registerRequest.email(),
                registerRequest.password(),
                Role.USER
        );
        Member member = memberRepository.insert(request);
        return new MemberResponse(member.getName());
    }

    private void validateUserLogin(Member member, LoginRequest loginRequest) {
        if (!member.getEmail().equals(loginRequest.email()) ||
                !member.getPassword().equals(loginRequest.password())) {
            throw new InvalidMemberException("비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED);
        }
    }
}
