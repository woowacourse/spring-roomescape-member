package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.User;
import roomescape.domain.UserRepository;
import roomescape.global.ReservationException;
import roomescape.service.param.LoginUserParam;
import roomescape.service.result.CheckLoginUserResult;
import roomescape.service.result.LoginUserResult;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginUserResult login(final LoginUserParam loginUserParam) {
        User user = userRepository.findByEmailAndPassword(loginUserParam.email(), loginUserParam.password())
                .orElseThrow(() -> new ReservationException(loginUserParam.email() + loginUserParam.password() + "에 해당하는 유저가 없습니다."));
        return LoginUserResult.from(user);
    }

    public CheckLoginUserResult findById(final Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ReservationException(id + "에 해당하는 유저가 없습니다."));
        return CheckLoginUserResult.from(user);
    }
}
