package roomescape.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.domain.DomainTerm;
import roomescape.common.domain.Email;
import roomescape.common.exception.NotFoundException;
import roomescape.user.application.dto.UserPublicInfoResponse;
import roomescape.user.domain.User;
import roomescape.user.domain.UserId;
import roomescape.user.domain.UserRepository;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public User getByEmail(final Email email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(DomainTerm.USER, email));
    }

    @Override
    public UserPublicInfoResponse getPubicInfoById(final UserId id) {
        return UserPublicInfoResponse.from(
                userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(DomainTerm.USER, id)));
    }
}
