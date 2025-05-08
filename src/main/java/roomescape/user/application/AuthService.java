package roomescape.user.application;

import org.springframework.stereotype.Service;
import roomescape.user.dto.TokenRequest;
import roomescape.user.infrastructure.TokenProvider;
import roomescape.user.infrastructure.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public String createToken(TokenRequest request) {
        String email = request.email();
        ensureExistsUser(email, request.password());
        return tokenProvider.createToken(email);
    }

    private void ensureExistsUser(String email, String password) {
        if(!userRepository.existedByEmailAndPassword(email, password)) {
            throw new AuthorizationException();
        }
    }
}
