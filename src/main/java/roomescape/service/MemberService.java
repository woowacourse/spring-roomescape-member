package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.request.UserLoginRequest;
import roomescape.controller.request.UserSignUpRequest;
import roomescape.controller.response.UserLoginResponse;
import roomescape.domain.User;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void save(UserSignUpRequest userSignUpRequest) {
        User user = userSignUpRequest.toEntity();

        memberRepository.save(user);
    }

    public UserLoginResponse createToken(UserLoginRequest userLoginRequest) {
        if (checkInvalidLogin(userLoginRequest.email(), userLoginRequest.password())) {
            throw new IllegalArgumentException();
        }

        String accessToken = jwtTokenProvider.createToken(userLoginRequest.email());
        return new UserLoginResponse(accessToken);
    }

    private boolean checkInvalidLogin(String email, String password) {
        return memberRepository.checkExistMember(email, password);
    }
}
