package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.request.UserLoginRequest;
import roomescape.controller.request.UserSignUpRequest;
import roomescape.controller.response.MemberResponse;
import roomescape.controller.response.UserResponse;
import roomescape.domain.User;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void save(UserSignUpRequest userSignUpRequest) {
        User user = userSignUpRequest.toEntity();

        memberRepository.save(user);
    }

    public String createToken(UserLoginRequest userLoginRequest) {
        if (!checkInvalidLogin(userLoginRequest.email(), userLoginRequest.password())) {
            throw new IllegalArgumentException("사용자 없음");
        }
        return jwtTokenProvider.createToken(userLoginRequest.email());
    }

    private boolean checkInvalidLogin(String email, String password) {
        return memberRepository.checkExistMember(email, password);
    }

    public UserResponse findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public List<MemberResponse> findAll() {
        List<User> users = memberRepository.findAll();

        return users.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
