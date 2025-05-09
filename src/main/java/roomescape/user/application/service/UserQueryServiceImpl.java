package roomescape.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.domain.DomainTerm;
import roomescape.common.exception.NotFoundException;
import roomescape.user.domain.UserRepository;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public String getPasswordByEmail(final String email) {
        return userRepository.findPasswordByEmail(email)
                .orElseThrow(() -> new NotFoundException(DomainTerm.USER, email));
    }
}
