package roomescape.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.auth.JwtProvider;
import roomescape.domain.User;
import roomescape.dto.SignupRequest;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    // TODO: password 암호화
    public String login(LoginRequest request) {
        User user = userRepository.getByEmail(request.email());
        if (!Objects.equals(user.password(), request.password())) {
            throw new IllegalArgumentException("잘못된 아이디 또는 패스워드입니다");
        }
        return jwtProvider.createToken(user);
    }

    public LoginCheckResponse loginCheck(Long id) {
        User user = userRepository.getById(id);
        return LoginCheckResponse.from(user);
    }

    public void signup(SignupRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        userRepository.save(new User(request.name(), request.email(), request.password()));
    }
}
