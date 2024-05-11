package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.application.dto.request.TokenCreationRequest;
import roomescape.application.dto.response.MemberResponse;
import roomescape.application.dto.response.TokenResponse;
import roomescape.auth.TokenProvider;
import roomescape.domain.user.User;
import roomescape.domain.user.repository.UserRepository;

@Service
public class MemberService {
    private static final String WRONG_EMAIL_OR_PASSWORD_MESSAGE = "등록되지 않은 이메일이거나 비밀번호가 틀렸습니다.";

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public MemberService(TokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    public TokenResponse authenticateMember(TokenCreationRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException(WRONG_EMAIL_OR_PASSWORD_MESSAGE));
        validatePassword(user, request.password());
        String token = tokenProvider.createToken(user.getId());
        return new TokenResponse(token);
    }

    private void validatePassword(User user, String password) {
        if (!user.matchPassword(password)) {
            throw new IllegalArgumentException(WRONG_EMAIL_OR_PASSWORD_MESSAGE);
        }
    }

    public MemberResponse getMemberById(long id) {
        User member = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return new MemberResponse(member.getName());
    }
}
