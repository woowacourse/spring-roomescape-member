package roomescape.service;

import jakarta.servlet.http.Cookie;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.request.UserLoginRequest;
import roomescape.controller.request.UserSignUpRequest;
import roomescape.controller.response.CheckMemberResponse;
import roomescape.controller.response.MemberResponse;
import roomescape.controller.response.TokenResponse;
import roomescape.domain.Member;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse save(UserSignUpRequest userSignUpRequest) {
        Member member = userSignUpRequest.toEntity();
        Member savedMember = memberRepository.save(member);
        return MemberResponse.from(member);
    }

    public TokenResponse createToken(UserLoginRequest userLoginRequest) {
        if (!checkInvalidLogin(userLoginRequest.email(), userLoginRequest.password())) {
            throw new IllegalArgumentException("사용자 없음");
        }
        Member byEmail = memberRepository.findByEmail(userLoginRequest.email());
        return new TokenResponse(jwtTokenProvider.createToken(byEmail));
    }

    private boolean checkInvalidLogin(String email, String password) {
        return memberRepository.checkExistMember(email, password);
    }

    public CheckMemberResponse findByEmail(String email) {
        Member byEmail = memberRepository.findByEmail(email);
        return new CheckMemberResponse(byEmail.getName());
    }

    public List<MemberResponse> findAll() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }

    public CheckMemberResponse findByCookies(Cookie[] cookies) {
        String email = jwtTokenProvider.getEmailByCookie(cookies);
        return findByEmail(email);
    }
}
