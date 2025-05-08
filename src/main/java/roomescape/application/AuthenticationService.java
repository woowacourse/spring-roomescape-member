package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.domain.User;
import roomescape.domain.repository.UserRepository;

@Service
public class AuthenticationService {

    private final AuthenticationTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthenticationService(final AuthenticationTokenProvider tokenProvider, final UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    public String issueToken(final String email, final String password) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다."));
        if (!user.matchesPassword(password)) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다.");
        }
        return tokenProvider.createToken(String.valueOf(user.id()));
    }

    public User findUserByToken(final String token) {
        var isValidToken = tokenProvider.isValidToken(token);
        if (!isValidToken) {
            throw new IllegalArgumentException("인증에 실패했습니다. 다시 로그인 해주세요.");
        }
        var id = Long.parseLong(tokenProvider.getPayload(token));
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다. 다시 로그인 해주세요."));
    }
}
