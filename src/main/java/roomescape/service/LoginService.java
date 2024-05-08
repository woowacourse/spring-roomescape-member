package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.JwtTokenProvider;
import roomescape.domain.Member;
import roomescape.exception.BadRequestException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.repository.user.MemberRepository;
import roomescape.service.dto.login.LoginCheckResponse;
import roomescape.service.dto.login.LoginRequest;
import roomescape.service.dto.login.LoginResponse;

@Service
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 멤버입니다."));
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
        String email = jwtTokenProvider.decode(token);
        Member member = findMemberByEmail(email);

        return LoginCheckResponse.from(member);
    }
}
