package roomescape.user.service;

import org.springframework.stereotype.Service;
import roomescape.auth.AuthService;
import roomescape.auth.TokenRequestDto;
import roomescape.auth.TokenResponseDto;
import roomescape.user.domain.dto.User;
import roomescape.user.domain.dto.UserRequestDto;
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
        userRepository.findUserByEmailAndPassword(tokenRequestDto.email(), tokenRequestDto.password());
        return authService.createToken(tokenRequestDto);
    }

    public User add(UserRequestDto userRequestDto) {
        User user = userRequestDto.toEntity();
        return userRepository.save(user);
    }
}
