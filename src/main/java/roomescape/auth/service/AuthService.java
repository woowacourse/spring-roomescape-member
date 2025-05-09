package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.domain.dto.TokenRequestDto;
import roomescape.auth.domain.dto.TokenResponseDto;
import roomescape.auth.exception.AuthorizationException;
import roomescape.user.domain.dto.User;
import roomescape.user.domain.dto.UserResponseDto;
import roomescape.user.exception.NotFoundUserException;
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

    public UserResponseDto findMember(String email) {
        User user = userRepository.findUseByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
        return UserResponseDto.of(user);
    }

    public UserResponseDto findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        userRepository.findUserByEmailAndPassword(tokenRequestDto.email(), tokenRequestDto.password())
                .orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));

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
