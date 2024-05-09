package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.member.domain.Member;
import roomescape.exception.BadRequestException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 멤버입니다."));
    }

    public Member findMemberByToken(String accessToken) {
        String email = jwtTokenProvider.decode(accessToken, "email");
        return findMemberByEmail(email);
    }

    public LoginResponse login(LoginRequest request) {
        Member member = findMemberByEmail(request.email());

        validatePassword(request, member);

        String accessToken = jwtTokenProvider.generate(member);
        return new LoginResponse(accessToken);
    }

    private void validatePassword(LoginRequest request, Member member) {
        if (!member.getPassword().equals(request.password())) {
            throw new BadRequestException("비밀번호가 잘못됐습니다.");
        }
    }

    public LoginCheckResponse checkLogin(String token) {
        String email = jwtTokenProvider.decode(token, "email");
        Member member = findMemberByEmail(email);

        return LoginCheckResponse.from(member);
    }
}
