package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.domain.dto.TokenRequestDto;
import roomescape.auth.domain.dto.TokenResponseDto;
import roomescape.auth.exception.InvalidTokenException;
import roomescape.globalException.AuthorizationException;
import roomescape.user.domain.User;
import roomescape.user.domain.dto.UserResponseDto;
import roomescape.user.exception.NotFoundUserException;
import roomescape.user.repository.UserRepository;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public boolean checkInvalidLogin(String email, String password) {
        return !userRepository.existUserByEmailAndPassword(email, password);
    }

    public UserResponseDto findMemberByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public UserResponseDto findMember(String email) {
        User user = userRepository.findUseByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        return UserResponseDto.of(user);
    }

    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        userRepository.findUserByEmailAndPassword(tokenRequestDto.email(), tokenRequestDto.password())
                .orElseThrow(NotFoundUserException::new);

        return createToken(tokenRequestDto);
    }

    public TokenResponseDto createToken(TokenRequestDto tokenRequestDto) {
        if (checkInvalidLogin(tokenRequestDto.email(), tokenRequestDto.password())) {
            throw new AuthorizationException("이메일 또는 패스워드가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(tokenRequestDto.email());
        return new TokenResponseDto(accessToken);
    }
}
