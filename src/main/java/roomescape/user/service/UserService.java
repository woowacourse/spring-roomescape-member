package roomescape.user.service;

import org.springframework.stereotype.Service;
import roomescape.auth.AuthService;
import roomescape.auth.TokenRequestDto;
import roomescape.auth.TokenResponseDto;
import roomescape.user.domain.dto.User;
import roomescape.user.domain.dto.UserRequestDto;
import roomescape.user.exception.NotFoundUserException;
import roomescape.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        userRepository.findUserByEmailAndPassword(tokenRequestDto.email(), tokenRequestDto.password())
                .orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));

        return authService.createToken(tokenRequestDto);
    }

    public User add(UserRequestDto userRequestDto) {
        User user = userRequestDto.toEntity();
        return userRepository.save(user);
    }
}
