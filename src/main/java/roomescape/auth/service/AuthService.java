package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.domain.dto.TokenInfoDto;
import roomescape.auth.domain.dto.TokenRequestDto;
import roomescape.auth.domain.dto.TokenResponseDto;
import roomescape.auth.exception.InvalidTokenException;
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

    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        User user = userRepository.findUserByEmailAndPassword(tokenRequestDto.email(), tokenRequestDto.password())
                .orElseThrow(NotFoundUserException::new);
        return createToken(TokenInfoDto.of(user));
    }

    public TokenResponseDto createToken(TokenInfoDto tokenInfoDto) {
        String accessToken = jwtTokenProvider.createToken(tokenInfoDto);
        return new TokenResponseDto(accessToken);
    }

    public UserResponseDto findMemberByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }
        String payload = jwtTokenProvider.getPayload(token);
        User user = findMember(payload);
        return UserResponseDto.of(user);
    }

    public User findMember(String payload) {
        Long id = Long.valueOf(payload);
        return userRepository.findById(id)
                .orElseThrow(NotFoundUserException::new);
    }
}
