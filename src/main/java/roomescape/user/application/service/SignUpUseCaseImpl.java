package roomescape.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.sign.password.PasswordEncoder;
import roomescape.user.application.dto.CreateUserServiceRequest;
import roomescape.user.domain.User;
import roomescape.user.domain.UserRole;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpUseCaseImpl implements SignUpUseCase {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User execute(final CreateUserServiceRequest request) {
        final String encodedPassword = passwordEncoder.execute(request.password());

        return User.withoutId(
                request.name(),
                request.email(),
                encodedPassword,
                UserRole.NORMAL);
    }
}
