package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.user.domain.dto.UserResponseDto;
import roomescape.user.repository.UserRepository;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public boolean checkInvalidLogin(String email, String password) {
        return !userRepository.existUserByEmailAndPassword(email, password);
    }

    public UserResponseDto findMember(String principal) {
        return null;
    }

    public UserResponseDto findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public TokenResponseDto createToken(TokenRequestDto tokenRequestDto) {
        if (checkInvalidLogin(tokenRequestDto.email(), tokenRequestDto.password())) {
            throw new AuthorizationException("이메일 또는 패스워드가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(tokenRequestDto.email());
        return new TokenResponseDto(accessToken);
    }
}
