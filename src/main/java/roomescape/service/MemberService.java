package roomescape.service;

import jakarta.servlet.http.Cookie;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.request.UserLoginRequest;
import roomescape.controller.request.UserSignUpRequest;
import roomescape.controller.response.MemberResponse;
import roomescape.controller.response.UserResponse;
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

    public void save(UserSignUpRequest userSignUpRequest) {
        Member member = userSignUpRequest.toEntity();

        memberRepository.save(member);
    }

    public String createToken(UserLoginRequest userLoginRequest) {
        if (!checkInvalidLogin(userLoginRequest.email(), userLoginRequest.password())) {
            throw new IllegalArgumentException("사용자 없음");
        }
        Member byEmail = memberRepository.findByEmail(userLoginRequest.email());
        return jwtTokenProvider.createToken(byEmail);
    }

    private boolean checkInvalidLogin(String email, String password) {
        return memberRepository.checkExistMember(email, password);
    }

    public UserResponse findByEmail(String email) {
        Member byEmail = memberRepository.findByEmail(email);
        return new UserResponse(byEmail.getId(), byEmail.getName(), byEmail.getEmail(), byEmail.getPassword(), byEmail.getRole());
    }

    public List<MemberResponse> findAll() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }

    public UserResponse findByCookies(Cookie[] cookies) {
        String token = jwtTokenProvider.extractTokenFromCookie(cookies);
        String email = jwtTokenProvider.getEmailByToken(token);
        return findByEmail(email);
    }
}
